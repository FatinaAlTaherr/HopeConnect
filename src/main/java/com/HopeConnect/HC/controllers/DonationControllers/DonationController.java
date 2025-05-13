package com.HopeConnect.HC.controllers.DonationControllers;

import com.HopeConnect.HC.DTO.ReviewDTO;
import com.HopeConnect.HC.models.Donation.*;
import com.HopeConnect.HC.models.User.User;
import com.HopeConnect.HC.services.DonationServices.DonationService;
import com.HopeConnect.HC.services.UserServices.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/HopeConnect/api/donations")
@RequiredArgsConstructor
public class DonationController {
    private final DonationService donationService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Donation> createDonation(@RequestBody Donation donation,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        User donor = userService.getUser(userDetails.getUsername());
        donation.setDonor(donor);
        return ResponseEntity.ok(donationService.createDonation(donation));
    }

    @GetMapping("/donor")
    public ResponseEntity<List<Donation>> getDonationsByDonor(@AuthenticationPrincipal UserDetails userDetails) {
        User donor = userService.getUser(userDetails.getUsername());
        return ResponseEntity.ok(donationService.getDonationsByDonor(donor));
    }

    @GetMapping("/orphanage/{orphanageId}")
    public ResponseEntity<List<Donation>> getDonationsByOrphanage(@PathVariable Long orphanageId) {
        return ResponseEntity.ok(donationService.getDonationsByOrphanage(
                userService.getOrphanageById(orphanageId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Donation> getDonationById(@PathVariable Long id) {
        return ResponseEntity.ok(donationService.getDonationById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Donation> updateDonationStatus(@PathVariable Long id,
                                                         @RequestParam DonationStatus status) {
        return ResponseEntity.ok(donationService.updateDonationStatus(id, status));
    }

    // Donation item endpoints
    @PostMapping("/{donationId}/items")
    public ResponseEntity<DonationItem> addDonationItem(@PathVariable Long donationId,
                                                        @RequestBody DonationItem item) {
        item.setDonation(donationService.getDonationById(donationId));
        return ResponseEntity.ok(donationService.addDonationItem(item));
    }

    @GetMapping("/{donationId}/items")
    public ResponseEntity<List<DonationItem>> getDonationItems(@PathVariable Long donationId) {
        return ResponseEntity.ok(donationService.getItemsByDonation(
                donationService.getDonationById(donationId)));
    }

    // Donation update endpoints
    @PostMapping("/{donationId}/updates")
    public ResponseEntity<DonationUpdate> addDonationUpdate(@PathVariable Long donationId,
                                                            @RequestBody DonationUpdate update) {
        update.setDonation(donationService.getDonationById(donationId));
        return ResponseEntity.ok(donationService.addDonationUpdate(update));
    }

    @GetMapping("/{donationId}/updates")
    public ResponseEntity<List<DonationUpdate>> getDonationUpdates(@PathVariable Long donationId) {
        return ResponseEntity.ok(donationService.getUpdatesByDonation(
                donationService.getDonationById(donationId)));
    }

    // Delivery tracking endpoints
    @PostMapping("/{donationId}/tracking")
    public ResponseEntity<DeliveryTracking> createDeliveryTracking(@PathVariable Long donationId,
                                                                   @RequestBody DeliveryTracking tracking) {
        tracking.setDonation(donationService.getDonationById(donationId));
        return ResponseEntity.ok(donationService.createDeliveryTracking(tracking));
    }

    @PutMapping("/tracking/{trackingId}")
    public ResponseEntity<DeliveryTracking> updateDeliveryLocation(@PathVariable Long trackingId,
                                                                   @RequestParam String location) {
        return ResponseEntity.ok(donationService.updateDeliveryTracking(trackingId, location));
    }

    @PostMapping("/{donationId}/reviews")
    public ResponseEntity<String> addReview(@PathVariable Long donationId,
                                            @RequestBody Review review,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        Donation donation = donationService.getDonationById(donationId);
        User donor = userService.getUser(userDetails.getUsername());

        review.setDonor(donor);
        review.setOrphanage(donation.getOrphanage());
        donationService.addReview(review);

        return ResponseEntity.ok("Your review has been sent.");
    }

    @GetMapping("/orphanage/{orphanageId}/reviews")
    public ResponseEntity<List<ReviewDTO>> getOrphanageReviews(@PathVariable Long orphanageId) {
        List<Review> reviews = donationService.getReviewsByOrphanage(
                userService.getOrphanageById(orphanageId));

        List<ReviewDTO> reviewDTOs = reviews.stream()
                .map(ReviewDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(reviewDTOs);
    }

    @GetMapping("/orphanage/{orphanageId}/rating")
    public ResponseEntity<Double> getOrphanageRating(@PathVariable Long orphanageId) {
        return ResponseEntity.ok(donationService.getAverageRating(
                userService.getOrphanageById(orphanageId)));
    }
}