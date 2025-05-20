package com.HopeConnect.HC.controllers.DonationControllers;

import com.HopeConnect.HC.DTO.DonorSummaryDTO;
import com.HopeConnect.HC.models.Donation.Donation;
import com.HopeConnect.HC.models.Donation.DonationCategory;
import com.HopeConnect.HC.models.Donation.DonationStatus;
import com.HopeConnect.HC.models.Donation.DonationUpdate;
import com.HopeConnect.HC.models.User.User;
import com.HopeConnect.HC.services.DonationServices.DonationService;
import com.HopeConnect.HC.services.UserServices.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/HopeConnect/api/dashboard/donor")
@RequiredArgsConstructor
public class DonorDashboardController {
    private final DonationService donationService;
    private final UserService userService;

    @GetMapping("/summary")
    @PreAuthorize("hasAnyAuthority('DONOR')")
    public ResponseEntity<DonorSummaryDTO> getDonorSummary(@AuthenticationPrincipal UserDetails userDetails) {
        User donor = userService.getUser(userDetails.getUsername());
        List<Donation> donations = donationService.getDonationsByDonor(donor);

        double totalDonated = donations.stream()
                .filter(d -> d.getStatus() == DonationStatus.COMPLETED || d.getStatus() == DonationStatus.DELIVERED)
                .mapToDouble(Donation::getAmount)
                .sum();

        long totalDonations = donations.size();
        long completedDonations = donations.stream()
                .filter(d -> d.getStatus() == DonationStatus.COMPLETED || d.getStatus() == DonationStatus.DELIVERED)
                .count();

        List<DonorSummaryDTO.RecentDonationDTO> recentDonations = donations.stream()
                .limit(5)
                .map(d -> new DonorSummaryDTO.RecentDonationDTO(
                        d.getId(),
                        d.getCategory(),
                        d.getAmount(),
                        d.getStatus(),
                        d.getCreatedAt()
                ))
                .toList();

        DonorSummaryDTO summary = new DonorSummaryDTO(
                totalDonated,
                totalDonations,
                completedDonations,
                recentDonations
        );

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/donations/updates")
    @PreAuthorize("hasAnyAuthority('DONOR')")
    public ResponseEntity<List<DonationUpdate>> getAllDonationUpdatesForUser(@AuthenticationPrincipal UserDetails userDetails) {
        User donor = userService.getUser(userDetails.getUsername());
        List<Donation> donations = donationService.getDonationsByDonor(donor);

        List<DonationUpdate> updates = donations.stream()
                .flatMap(donation -> donationService.getUpdatesByDonation(donation).stream())
                .toList();

        return ResponseEntity.ok(updates);
    }

    @GetMapping("/impact")
    @PreAuthorize("hasAnyAuthority('DONOR')")
    public ResponseEntity<Map<String, Object>> getDonorImpact(@AuthenticationPrincipal UserDetails userDetails) {
        User donor = userService.getUser(userDetails.getUsername());
        List<Donation> donations = donationService.getDonationsByDonor(donor);

        Map<DonationCategory, Double> categoryImpact = donations.stream()
                .filter(d -> d.getStatus() == DonationStatus.COMPLETED || d.getStatus() == DonationStatus.DELIVERED)
                .collect(Collectors.groupingBy(
                        Donation::getCategory,
                        Collectors.summingDouble(Donation::getAmount)
                ));

        return ResponseEntity.ok(Map.of(
                "categoryImpact", categoryImpact,
                "supportedOrphanages", donations.stream()
                        .map(Donation::getOrphanage)
                        .distinct()
                        .count()
        ));
    }
}
