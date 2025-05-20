package com.HopeConnect.HC.controllers.DonationControllers;

import com.HopeConnect.HC.DTO.ReviewDTO;
import com.HopeConnect.HC.DTO.StripeResponse;
import com.HopeConnect.HC.models.Donation.*;
import com.HopeConnect.HC.models.User.User;
import com.HopeConnect.HC.repositories.DonationRepositories.DonationRepository;
import com.HopeConnect.HC.services.DonationServices.DonationService;
import com.HopeConnect.HC.services.StripeServices.DonationPaymentService;
import com.HopeConnect.HC.services.UserServices.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/HopeConnect/api/donations")
@RequiredArgsConstructor
public class DonationController {
    private final DonationService donationService;
    private final UserService userService;
    private final DonationPaymentService donationPaymentService;
    private final DonationRepository donationRepository;

    private final Map<String, Long> sessionDonationMap = new ConcurrentHashMap<>();

    @PostMapping("/create-session/{donationId}")
    @PreAuthorize("hasAnyAuthority('DONOR')")
    public ResponseEntity<StripeResponse> createPaymentSession(@PathVariable Long donationId) {
        Donation donation = donationService.getDonationById(donationId);
        StripeResponse response = donationPaymentService.createDonationSession(donation);
        if ("SUCCESS".equals(response.getStatus())) {
            donation.setPaymentIntent(response.getSessionId());
            donationService.saveDonation(donation);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/success")
    @PreAuthorize("hasAnyAuthority('DONOR')")
    public ResponseEntity<String> donationSuccess(@RequestParam("session_id") String sessionId) {
        try {
            donationPaymentService.confirmSuccessfulPayment(sessionId);
            Session session = Session.retrieve(sessionId);
            String donationIdStr = session.getMetadata().get("donationId");
            if (donationIdStr == null) {
                return ResponseEntity.badRequest().body("Donation ID not found in session");
            }
            Long donationId = Long.parseLong(donationIdStr);
            Donation donation = donationService.getDonationById(donationId);
            donation.setStatus(DonationStatus.COMPLETED);
            donationService.saveDonation(donation);
            return ResponseEntity.ok("Donation processed successfully!");
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error verifying payment: " + e.getMessage());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid donation ID format");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing donation: " + e.getMessage());
        }
    }

    @GetMapping("/cancel")
    @PreAuthorize("hasAnyAuthority('DONOR')")
    public String donationCancelled() {
        return "Donation payment was cancelled. You can try again if you wish.";
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('DONOR')")
    public ResponseEntity<Donation> createDonation(@RequestBody Donation donation,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        User donor = userService.getUser(userDetails.getUsername());
        donation.setDonor(donor);
        return ResponseEntity.ok(donationService.createDonation(donation));
    }

    @GetMapping("/donor")
    @PreAuthorize("hasAnyAuthority('DONOR')")
    public ResponseEntity<List<Donation>> getDonationsByDonor(@AuthenticationPrincipal UserDetails userDetails) {
        User donor = userService.getUser(userDetails.getUsername());
        return ResponseEntity.ok(donationService.getDonationsByDonor(donor));
    }

    @GetMapping("/orphanage/{orphanageId}")
    @PreAuthorize("hasAnyAuthority('ORPHANAGE_OWNER', 'ADMIN')")
    public ResponseEntity<List<Donation>> getDonationsByOrphanage(@PathVariable Long orphanageId) {
        return ResponseEntity.ok(donationService.getDonationsByOrphanage(
                userService.getOrphanageById(orphanageId)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DONOR', 'ORPHANAGE_OWNER')")
    public ResponseEntity<Donation> getDonationById(@PathVariable Long id) {
        return ResponseEntity.ok(donationService.getDonationById(id));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Donation> updateDonationStatus(@PathVariable Long id,
                                                         @RequestParam DonationStatus status) {
        return ResponseEntity.ok(donationService.updateDonationStatus(id, status));
    }

    @PostMapping("/{donationId}/items")
    @PreAuthorize("hasAnyAuthority('DONOR')")
    public ResponseEntity<DonationItem> addDonationItem(@PathVariable Long donationId,
                                                        @RequestBody DonationItem item) {
        item.setDonation(donationService.getDonationById(donationId));
        return ResponseEntity.ok(donationService.addDonationItem(item));
    }

    @GetMapping("/{donationId}/items")
    @PreAuthorize("hasAnyAuthority('DONOR', 'ORPHANAGE_OWNER')")
    public ResponseEntity<List<DonationItem>> getDonationItems(@PathVariable Long donationId) {
        return ResponseEntity.ok(donationService.getItemsByDonation(
                donationService.getDonationById(donationId)));
    }

    @PostMapping("/{donationId}/updates")
    @PreAuthorize("hasAnyAuthority('ORPHANAGE_OWNER')")
    public ResponseEntity<DonationUpdate> addDonationUpdate(@PathVariable Long donationId,
                                                            @RequestBody DonationUpdate update) {
        update.setDonation(donationService.getDonationById(donationId));
        return ResponseEntity.ok(donationService.addDonationUpdate(update));
    }

    @GetMapping("/{donationId}/updates")
    @PreAuthorize("hasAnyAuthority('DONOR', 'ORPHANAGE_OWNER')")
    public ResponseEntity<List<DonationUpdate>> getDonationUpdates(@PathVariable Long donationId) {
        return ResponseEntity.ok(donationService.getUpdatesByDonation(
                donationService.getDonationById(donationId)));
    }

    @PostMapping("/{donationId}/tracking")
    @PreAuthorize("hasAnyAuthority('ORPHANAGE_OWNER')")
    public ResponseEntity<DeliveryTracking> createDeliveryTracking(@PathVariable Long donationId,
                                                                   @RequestBody DeliveryTracking tracking) {
        tracking.setDonation(donationService.getDonationById(donationId));
        return ResponseEntity.ok(donationService.createDeliveryTracking(tracking));
    }

    @PutMapping("/tracking/{trackingId}")
    @PreAuthorize("hasAnyAuthority('ORPHANAGE_OWNER')")
    public ResponseEntity<DeliveryTracking> updateDeliveryLocation(@PathVariable Long trackingId,
                                                                   @RequestParam String location) {
        return ResponseEntity.ok(donationService.updateDeliveryTracking(trackingId, location));
    }

    @PostMapping("/{donationId}/reviews")
    @PreAuthorize("hasAnyAuthority('DONOR')")
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
    @PreAuthorize("hasAnyAuthority('ORPHANAGE_OWNER', 'ADMIN')")
    public ResponseEntity<List<ReviewDTO>> getOrphanageReviews(@PathVariable Long orphanageId) {
        List<Review> reviews = donationService.getReviewsByOrphanage(
                userService.getOrphanageById(orphanageId));
        List<ReviewDTO> reviewDTOs = reviews.stream()
                .map(ReviewDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviewDTOs);
    }

    @GetMapping("/orphanage/{orphanageId}/rating")
    @PreAuthorize("hasAnyAuthority('ORPHANAGE_OWNER', 'ADMIN')")
    public ResponseEntity<Double> getOrphanageRating(@PathVariable Long orphanageId) {
        return ResponseEntity.ok(donationService.getAverageRating(
                userService.getOrphanageById(orphanageId)));
    }
}
