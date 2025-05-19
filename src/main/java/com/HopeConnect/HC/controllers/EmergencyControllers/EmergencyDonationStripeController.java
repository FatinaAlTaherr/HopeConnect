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

    // In-memory map to store sessionId -> donorEmail (for demo only)
    private final Map<String, String> sessionEmailMap = new ConcurrentHashMap<>();

    @PostMapping("/checkout")
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
    public ResponseEntity<String> donationSuccess(@RequestParam("session_id") String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);

            System.out.println("=== Stripe Session Details ===");
            System.out.println("Session ID: " + session.getId());
            System.out.println("Customer Email: " + session.getCustomerEmail());
            System.out.println("Amount Total: " + session.getAmountTotal());
            System.out.println("Currency: " + session.getCurrency());
            System.out.println("Metadata: " + session.getMetadata());
            System.out.println("===============================");

            // Get donor email from stored map instead of session metadata
            String donorEmail = sessionEmailMap.get(sessionId);

            if (donorEmail == null || donorEmail.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No donor email found for this session.");
            }

            User donor = userRepository.findByEmail(donorEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // You must have stored campaignId in the request or session metadata,
            // but since it's missing in your code snippet,
            // assume you add it to metadata or request and fetch it here:
            String campaignIdStr = session.getMetadata().get("campaignId");
            if (campaignIdStr == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No campaign ID found for this session.");
            }
            Long campaignId = Long.parseLong(campaignIdStr);

            EmergencyCampaign campaign = emergencyCampaignRepository.findById(campaignId)
                    .orElseThrow(() -> new RuntimeException("Campaign not found"));

            // Amount is in smallest currency unit (e.g. cents), convert to double
            double amountDonated = session.getAmountTotal() / 100.0;

            // Create EmergencyDonation entity and save
            EmergencyDonation donation = EmergencyDonation.builder()
                    .donor(donor)
                    .campaign(campaign)
                    .amount(amountDonated)
                    .donationDate(LocalDateTime.now())
                    .paymentIntentId(session.getPaymentIntent())
                    .build();

            emergencyDonationRepository.save(donation);

            // Update campaign currentAmount
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
    public String donationCancelled() {
        return "Donation cancelled.";
    }
}
