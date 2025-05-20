package com.HopeConnect.HC.controllers.EmergencyControllers;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.security.access.prepost.PreAuthorize;
import com.HopeConnect.HC.DTO.EmergencyCampaignResponseDTO;
import com.HopeConnect.HC.DTO.EmergencyDonationResponseDTO;
import com.HopeConnect.HC.models.EmergencyCampaign.EmergencyCampaign;
import com.HopeConnect.HC.models.EmergencyCampaign.EmergencyDonation;
import com.HopeConnect.HC.models.User.User;
import com.HopeConnect.HC.services.EmergencyServices.EmergencyService;
import com.HopeConnect.HC.services.UserServices.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/HopeConnect/api/emergency")
@RequiredArgsConstructor
public class EmergencyController {

    private final EmergencyService emergencyService;
    private final UserService userService;

    // For orphanage owners to create campaigns
    @PostMapping("/campaigns")
    public ResponseEntity<EmergencyCampaignResponseDTO> createCampaign(
            @RequestBody EmergencyCampaign campaign,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.getUser(userDetails.getUsername());
        EmergencyCampaign created = emergencyService.createCampaign(campaign, user);
        return ResponseEntity.ok(EmergencyCampaignResponseDTO.fromCampaign(created));
    }

    @GetMapping("/campaigns")
    public ResponseEntity<List<EmergencyCampaignResponseDTO>> getActiveCampaigns() {
        List<EmergencyCampaign> campaigns = emergencyService.getActiveCampaigns();
        List<EmergencyCampaignResponseDTO> response = campaigns.stream()
                .map(EmergencyCampaignResponseDTO::fromCampaign)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/campaigns/{campaignId}/donate")
    public ResponseEntity<EmergencyDonationResponseDTO> makeDonation(
            @PathVariable Long campaignId,
            @RequestParam Double amount,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.getUser(userDetails.getUsername());
        EmergencyDonation donation = emergencyService.processDonation(campaignId, amount, user);

        EmergencyDonationResponseDTO response = EmergencyDonationResponseDTO.fromDonation(donation);
        return ResponseEntity.ok(response);
    }


    // For orphanage owners to see their campaigns
    @GetMapping("/my-campaigns")
    public ResponseEntity<List<EmergencyCampaignResponseDTO>> getOrphanageCampaigns(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUser(userDetails.getUsername());
        List<EmergencyCampaign> campaigns = emergencyService.getOrphanageCampaigns(user);
        List<EmergencyCampaignResponseDTO> response = campaigns.stream()
                .map(EmergencyCampaignResponseDTO::fromCampaign)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/alerts")
    public ResponseEntity<String> sendEmergencyAlert(
            @RequestBody Map<String, String> payload,
            @AuthenticationPrincipal UserDetails userDetails) {

        String subject = payload.get("subject");
        String message = payload.get("message");

        if (subject == null || message == null) {
            return ResponseEntity.badRequest().body("Subject and message are required");
        }

        User sender = userService.getUser(userDetails.getUsername());

        emergencyService.sendEmergencyAlertToAllUsers(subject, message);
        return ResponseEntity.ok("Emergency alert sent to donor users.");
    }

    @PostMapping("/campaigns/check-expired")
    public ResponseEntity<String> manuallyCheckExpiredCampaigns() {
        emergencyService.checkExpiredCampaigns();
        return ResponseEntity.ok("Expired campaigns checked and processed.");
    }


}
