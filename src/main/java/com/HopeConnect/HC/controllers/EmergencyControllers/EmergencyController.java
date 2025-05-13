package com.HopeConnect.HC.controllers.EmergencyControllers;

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

import java.util.List;

@RestController
@RequestMapping("/HopeConnect/api/emergency")
@RequiredArgsConstructor
public class EmergencyController {
    private final EmergencyService emergencyService;
    private final UserService userService;

    // For orphanage owners to create campaigns
    @PostMapping("/campaigns")
    public ResponseEntity<EmergencyCampaign> createCampaign(
            @RequestBody EmergencyCampaign campaign,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUser(userDetails.getUsername());
        return ResponseEntity.ok(emergencyService.createCampaign(campaign, user));
    }

    // Get all active campaigns
    @GetMapping("/campaigns")
    public ResponseEntity<List<EmergencyCampaign>> getActiveCampaigns() {
        return ResponseEntity.ok(emergencyService.getActiveCampaigns());
    }

    // Make a donation
    @PostMapping("/campaigns/{campaignId}/donate")
    public ResponseEntity<EmergencyDonation> makeDonation(
            @PathVariable Long campaignId,
            @RequestParam Double amount,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUser(userDetails.getUsername());
        return ResponseEntity.ok(emergencyService.processDonation(campaignId, amount, user));
    }

    // For orphanage owners to see their campaigns
    @GetMapping("/my-campaigns")
    public ResponseEntity<List<EmergencyCampaign>> getOrphanageCampaigns(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUser(userDetails.getUsername());
        return ResponseEntity.ok(emergencyService.getOrphanageCampaigns(user));
    }

    @PostMapping("/alerts")
    public ResponseEntity<String> sendEmergencyAlert(
            @RequestParam String subject,
            @RequestParam String message,
            @AuthenticationPrincipal UserDetails userDetails) {

        User sender = userService.getUser(userDetails.getUsername());

        // Corrected string comparison
        if (!"ADMIN".equals(sender.getRole())) {
            return ResponseEntity.status(403).body("Access denied");
        }

        emergencyService.sendEmergencyAlertToAllUsers(subject, message);
        return ResponseEntity.ok("Emergency alert sent to all users.");
    }


}