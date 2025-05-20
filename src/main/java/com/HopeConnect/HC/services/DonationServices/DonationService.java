// DonationService.java
package com.HopeConnect.HC.services.DonationServices;

import com.HopeConnect.HC.models.Donation.*;
import com.HopeConnect.HC.models.OrphanManagement.Orphanage;
import com.HopeConnect.HC.models.User.User;
import com.HopeConnect.HC.repositories.DonationItemRepositories.DonationItemRepository;
import com.HopeConnect.HC.repositories.DonationRepositories.DeliveryTrackingRepository;
import com.HopeConnect.HC.repositories.DonationRepositories.DonationRepository;
import com.HopeConnect.HC.repositories.DonationRepositories.DonationUpdateRepository;
import com.HopeConnect.HC.repositories.DonationRepositories.ReviewRepository;
import com.HopeConnect.HC.services.EmailSenderService;
import com.HopeConnect.HC.services.StripeServices.DonationPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonationService {
    private final DonationRepository donationRepository;
    private final DonationItemRepository donationItemRepository;
    private final DonationUpdateRepository donationUpdateRepository;
    private final DeliveryTrackingRepository deliveryTrackingRepository;
    private final ReviewRepository reviewRepository;
    private final DonationPaymentService paymentService;
    private final EmailSenderService emailSenderService;

    private final FeeConfig feeConfig;
    public Donation saveDonation(Donation donation) {
        return donationRepository.save(donation);
    }



    public Donation createDonation(Donation donation) {
        if (donation.getType() == DonationType.MONEY) {
            // Calculate and set fees for monetary donations
            double fee = calculateTransactionFee(donation.getAmount());
            donation.setTransactionFee(fee);
            donation.setNetAmount(donation.getAmount() - fee); // Subtract fee from amount for money donations
        } else {
            // Apply fixed fee for non-money donations
            Double nonMoneyDonationFixedFee= 2.0;
            donation.setTransactionFee(nonMoneyDonationFixedFee);
            donation.setAmount(0.0);
            donation.setNetAmount(donation.getAmount() + nonMoneyDonationFixedFee);
            donation.setAmount(0.0);
        }

        Donation savedDonation = donationRepository.save(donation);

        if (donation.getType() == DonationType.MONEY) {
            try {
                String clientSecret = String.valueOf(paymentService.createDonationSession(donation));
                savedDonation.setPaymentIntent(clientSecret);
                donationRepository.save(savedDonation);
            } catch (Exception e) {
            }
        }

        sendDonationConfirmation(savedDonation);
        return savedDonation;
    }

    private double calculateTransactionFee(double amount) {
        double calculatedFee = amount * (feeConfig.getTransactionFeePercentage() / 100);
        calculatedFee = Math.max(calculatedFee, feeConfig.getMinimumFee());
        return Math.min(calculatedFee, feeConfig.getMaximumFee());
    }

    public List<Donation> getDonationsByDonor(User donor) {
        return donationRepository.findByDonor(donor);
    }

    public List<Donation> getDonationsByOrphanage(Orphanage orphanage) {
        return donationRepository.findByOrphanage(orphanage);
    }

    public Donation getDonationById(Long id) {
        return donationRepository.findById(id).orElse(null);
    }

    public Donation updateDonationStatus(Long id, DonationStatus status) {
        return donationRepository.findById(id).map(donation -> {
            donation.setStatus(status);
            return donationRepository.save(donation);
        }).orElse(null);
    }

    // Donation item methods
    public DonationItem addDonationItem(DonationItem item) {
        if (item.getName() == null || item.getName().isBlank()) {
            throw new IllegalArgumentException("Item name is required");
        }
        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        return donationItemRepository.save(item);
    }

    public List<DonationItem> getItemsByDonation(Donation donation) {
        return donationItemRepository.findByDonation(donation);
    }

    // Donation update methods
    public DonationUpdate addDonationUpdate(DonationUpdate update) {
        update.setUpdateDate(LocalDateTime.now());
        return donationUpdateRepository.save(update);
    }

    public List<DonationUpdate> getUpdatesByDonation(Donation donation) {
        return donationUpdateRepository.findByDonation(donation);
    }

    // Delivery tracking methods
    public DeliveryTracking createDeliveryTracking(DeliveryTracking tracking) {
        return deliveryTrackingRepository.save(tracking);
    }

    public DeliveryTracking updateDeliveryTracking(Long id, String location) {
        return deliveryTrackingRepository.findById(id).map(tracking -> {
            tracking.setLocation(location);
            return deliveryTrackingRepository.save(tracking);
        }).orElse(null);
    }

    // Review methods
    public Review addReview(Review review) {
        review.setReviewDate(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByOrphanage(Orphanage orphanage) {
        return reviewRepository.findByOrphanage(orphanage);
    }

    public Double getAverageRating(Orphanage orphanage) {
        return reviewRepository.findAverageRatingByOrphanage(orphanage);
    }


    public Donation confirmDonationPayment(Long donationId, String paymentIntentId) {
        try {
            paymentService.confirmSuccessfulPayment(paymentIntentId);

            Donation donation = donationRepository.findById(donationId)
                    .orElseThrow(() -> new RuntimeException("Donation not found"));

            donation.setStatus(DonationStatus.COMPLETED);
            donation.setPaymentIntent(paymentIntentId);

            sendOrphanageNotification(donation);
            return donationRepository.save(donation);
        } catch (Exception e) {
            throw new RuntimeException("Payment confirmation failed: " + e.getMessage());
        }
    }

    private void sendOrphanageNotification(Donation donation) {
        String subject = "New Donation Received";
        String body = "Dear " + donation.getOrphanage().getOwner().getUsername() + ",\n\n" +
                "You have received a new donation of " + donation.getNetAmount() +
                " from " + donation.getDonor().getUsername() + ".\n\n" +
                "Donation details:\n" +
                "Type: " + donation.getType() + "\n" +
                "Category: " + donation.getCategory() + "\n" +
                "Description: " + (donation.getDescription() != null ? donation.getDescription() : "None") + "\n\n" +
                "Thank you for using HopeConnect!\n\n" +
                "Best regards,\n" +
                "HopeConnect Team";

        emailSenderService.sendEmail(donation.getOrphanage().getEmail(), subject, body);
    }
    // Helper methods
    private void sendDonationConfirmation(Donation donation) {
        String subject = "Donation Confirmation";
        String body = "Dear " + donation.getDonor().getUsername() + ",\n\n" +
                "Thank you for your donation of " + donation.getAmount() +
                " to " + donation.getOrphanage().getName() + ".\n";

        if (donation.getType() == DonationType.MONEY) {
            body += "Transaction fee: " + donation.getTransactionFee() + "\n" +
                    "Net amount received by orphanage: " + donation.getNetAmount() + "\n";
        }

        body += "Your donation will help us " + getCategoryImpact(donation.getCategory()) + ".\n\n" +
                "You can track your donation status in your donor dashboard.\n\n" +
                "Best regards,\n" +
                "HopeConnect Team";

        emailSenderService.sendEmail(donation.getDonor().getEmail(), subject, body);
    }



    private String getCategoryImpact(DonationCategory category) {
        switch (category) {
            case GENERAL_FUND:
                return "support the daily needs of children in orphanages";
            case EDUCATION_SUPPORT:
                return "provide education and school supplies to children";
            case MEDICAL_AID:
                return "cover healthcare costs for children in need";
            case CLOTHING:
                return "provide clothing to children";
            case FOOD:
                return "provide nutritious meals to children";
            case EDUCATIONAL_MATERIALS:
                return "provide books and learning materials";
            default:
                return "make a difference in children's lives";
        }
    }



}