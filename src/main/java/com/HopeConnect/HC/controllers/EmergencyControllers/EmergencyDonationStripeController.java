package com.HopeConnect.HC.controllers.EmergencyControllers;

import com.HopeConnect.HC.DTO.EmergencyDonationRequest;
import com.HopeConnect.HC.DTO.StripeResponse;
import com.HopeConnect.HC.models.EmergencyCampaign.EmergencyCampaign;
import com.HopeConnect.HC.models.EmergencyCampaign.EmergencyDonation;
import com.HopeConnect.HC.models.User.User;
import com.HopeConnect.HC.repositories.EmergencyCampaignRepositories.EmergencyCampaignRepository;
import com.HopeConnect.HC.repositories.EmergencyCampaignRepositories.EmergencyDonationRepository;
import com.HopeConnect.HC.repositories.UserRepository;
import com.HopeConnect.HC.services.StripeServices.EmergencyStripeService;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/emergency")
@RequiredArgsConstructor
public class EmergencyDonationStripeController {

    private final EmergencyStripeService stripeService;
    private final EmergencyCampaignRepository emergencyCampaignRepository;
    private final EmergencyDonationRepository emergencyDonationRepository;
    private final UserRepository userRepository;

    private final Map<String, String> sessionEmailMap = new ConcurrentHashMap<>();

    @PostMapping("/checkout")
    @PreAuthorize("hasAnyAuthority('DONOR', 'SPONSOR')")
    public ResponseEntity<StripeResponse> createStripeSession(@RequestBody EmergencyDonationRequest request) {
        StripeResponse response = stripeService.createDonationSession(request);

        if ("SUCCESS".equals(response.getStatus())
                && response.getSessionId() != null
                && request.getDonorEmail() != null) {
            sessionEmailMap.put(response.getSessionId(), request.getDonorEmail());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/success")
    @PreAuthorize("hasAnyAuthority('DONOR', 'SPONSOR')")
    public ResponseEntity<String> donationSuccess(@RequestParam("session_id") String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);

            String donorEmail = sessionEmailMap.get(sessionId);

            if (donorEmail == null || donorEmail.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No donor email found for this session.");
            }

            User donor = userRepository.findByEmail(donorEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String campaignIdStr = session.getMetadata().get("campaignId");
            if (campaignIdStr == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No campaign ID found for this session.");
            }
            Long campaignId = Long.parseLong(campaignIdStr);

            EmergencyCampaign campaign = emergencyCampaignRepository.findById(campaignId)
                    .orElseThrow(() -> new RuntimeException("Campaign not found"));

            double amountDonated = session.getAmountTotal() / 100.0;

            EmergencyDonation donation = EmergencyDonation.builder()
                    .donor(donor)
                    .campaign(campaign)
                    .amount(amountDonated)
                    .donationDate(LocalDateTime.now())
                    .paymentIntentId(session.getPaymentIntent())
                    .build();

            emergencyDonationRepository.save(donation);

            if (campaign.getCurrentAmount() == null) {
                campaign.setCurrentAmount(0.0);
            }
            campaign.setCurrentAmount(campaign.getCurrentAmount() + amountDonated);
            emergencyCampaignRepository.save(campaign);

            return ResponseEntity.ok("Donation successful! Thank you, " + donor.getUsername());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Donation processing failed: " + e.getMessage());
        }
    }

    @GetMapping("/cancel")
    @PreAuthorize("hasAnyAuthority('DONOR', 'SPONSOR')")
    public String donationCancelled() {
        return "Donation cancelled.";
    }
}
