package com.HopeConnect.HC.controllers.DonationControllers;

import com.HopeConnect.HC.DTO.DonationSummaryDTO;
import com.HopeConnect.HC.DTO.DonationUpdateResponseDTO;
import com.HopeConnect.HC.models.Donation.Donation;
import com.HopeConnect.HC.models.Donation.DonationCategory;
import com.HopeConnect.HC.models.Donation.DonationStatus;
import com.HopeConnect.HC.models.Donation.DonationUpdate;
import com.HopeConnect.HC.models.OrphanManagement.Orphanage;
import com.HopeConnect.HC.models.User.User;
import com.HopeConnect.HC.services.DonationServices.DonationService;
import com.HopeConnect.HC.services.OrphanManagementServices.OrphanageService;
import com.HopeConnect.HC.services.UserServices.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/HopeConnect/api/dashboard/orphanage")
@RequiredArgsConstructor
public class OrphanageDashboardController {
    private final DonationService donationService;
    private final OrphanageService orphanageService;
    private final UserService userService;
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getOrphanageSummary(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUser(userDetails.getUsername());
        Orphanage orphanage = orphanageService.getOrphanageByUser(user);
        List<Donation> donations = donationService.getDonationsByOrphanage(orphanage);

        double totalReceived = donations.stream()
                .filter(d -> d.getStatus() == DonationStatus.COMPLETED || d.getStatus() == DonationStatus.DELIVERED)
                .mapToDouble(Donation::getAmount)
                .sum();

        long totalDonations = donations.size();
        long pendingDonations = donations.stream()
                .filter(d -> d.getStatus() == DonationStatus.PENDING)
                .count();

        List<DonationSummaryDTO> recentDonations = donations.stream()
                .limit(5)
                .map(DonationSummaryDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of(
                "totalReceived", totalReceived,
                "totalDonations", totalDonations,
                "pendingDonations", pendingDonations,
                "recentDonations", recentDonations,
                "averageRating", donationService.getAverageRating(orphanage)
        ));
    }


    @GetMapping("/donations/category")
    public ResponseEntity<Map<DonationCategory, Double>> getDonationsByCategory(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUser(userDetails.getUsername());
        Orphanage orphanage = orphanageService.getOrphanageByUser(user);
        List<Donation> donations = donationService.getDonationsByOrphanage(orphanage);

        Map<DonationCategory, Double> categoryDistribution = donations.stream()
                .filter(d -> d.getStatus() == DonationStatus.COMPLETED || d.getStatus() == DonationStatus.DELIVERED)
                .collect(Collectors.groupingBy(
                        Donation::getCategory,
                        Collectors.summingDouble(Donation::getAmount)
                ));

        return ResponseEntity.ok(categoryDistribution);
    }

    @PostMapping("/donations/{donationId}/updates")
    public ResponseEntity<DonationUpdateResponseDTO> addDonationUpdate(
            @PathVariable Long donationId,
            @RequestBody DonationUpdate update,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUser(userDetails.getUsername());
        Orphanage orphanage = orphanageService.getOrphanageByUser(user);
        Donation donation = donationService.getDonationById(donationId);

        if (!donation.getOrphanage().equals(orphanage)) {
            throw new IllegalArgumentException("You can only add updates to your orphanage's donations");
        }

        update.setDonation(donation);
        DonationUpdate savedUpdate = donationService.addDonationUpdate(update);

        return ResponseEntity.ok(new DonationUpdateResponseDTO(
                "Update added successfully",
                savedUpdate.getTitle(),
                savedUpdate.getDescription(),
                savedUpdate.getImageUrl()
        ));
    }

}