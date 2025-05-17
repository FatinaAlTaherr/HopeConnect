package com.HopeConnect.HC.services.EmergencyServices;

import com.HopeConnect.HC.models.EmergencyCampaign.EmergencyCampaign;
import com.HopeConnect.HC.models.EmergencyCampaign.EmergencyDonation;
import com.HopeConnect.HC.models.OrphanManagement.Orphanage;
import com.HopeConnect.HC.models.User.Role;
import com.HopeConnect.HC.models.User.User;
import com.HopeConnect.HC.repositories.EmergencyCampaignRepositories.EmergencyCampaignRepository;
import com.HopeConnect.HC.repositories.EmergencyCampaignRepositories.EmergencyDonationRepository;
import com.HopeConnect.HC.services.OrphanManagementServices.OrphanageService;
import com.HopeConnect.HC.services.UserServices.UserService;
import com.HopeConnect.HC.services.EmailSenderService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmergencyService {
    private final EmergencyCampaignRepository campaignRepository;
    private final EmergencyDonationRepository donationRepository;
    private final OrphanageService orphanageService;
    private final UserService userService;
    private final EmailSenderService emailSenderService; // Replaced EmailService

    // Campaign methods
    public EmergencyCampaign createCampaign(EmergencyCampaign campaign, User orphanageOwner) {
        Orphanage orphanage = orphanageService.getOrphanageByUser(orphanageOwner);
        campaign.setOrphanage(orphanage);
        campaign.setCurrentAmount(0.0);
        campaign.setActive(true);
        return campaignRepository.save(campaign);
    }

    public List<EmergencyCampaign> getActiveCampaigns() {
        return campaignRepository.findByIsActiveTrueAndDeadlineAfter(LocalDateTime.now());
    }

    public EmergencyDonation processDonation(Long campaignId, Double amount, User donor) {
        EmergencyCampaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        if (!campaign.isActive()) {
            throw new RuntimeException("Campaign is no longer active");
        }

        // Update campaign amount
        double updatedAmount = (campaign.getCurrentAmount() != null ? campaign.getCurrentAmount() : 0.0) + amount;
        campaign.setCurrentAmount(updatedAmount);

        // Optionally, deactivate if target met
        if (updatedAmount >= campaign.getTargetAmount()) {
            campaign.setActive(false);
        }

        campaignRepository.save(campaign); // Save updated campaign

        EmergencyDonation donation = EmergencyDonation.builder()
                .donor(donor)
                .campaign(campaign)
                .amount(amount)
                .donationDate(LocalDateTime.now())
                // .paymentIntentId() – You can optionally set this from Stripe
                .build();

        return donationRepository.save(donation);
    }

    public void handleSuccessfulDonation(Long campaignId, Double amount) {
        EmergencyCampaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        campaign.setCurrentAmount(campaign.getCurrentAmount() + amount);
        campaignRepository.save(campaign);
    }

    private void sendDonationNotification(User donor, EmergencyCampaign campaign, Double amount) {
        String subject = "Thank you for your emergency donation";
        String content = String.format(
                "Dear %s,\n\nThank you for donating $%.2f to our '%s' campaign.\n\n" +
                        "Your contribution will help us address: %s",
                donor.getUsername(),
                amount,
                campaign.getTitle(),
                campaign.getDescription()
        );

        emailSenderService.sendEmail(donor.getEmail(), subject, content);
    }

    public void sendEmergencyAlertToAllUsers(String subject, String message) {
        List<User> donorUsers = userService.getUsersByRole(Role.DONOR);
        donorUsers.forEach(user -> emailSenderService.sendEmail(user.getEmail(), subject, message));
    }

    public PaymentIntent createPaymentIntent(Long campaignId, Double amount, User donor) throws StripeException, StripeException {
        EmergencyCampaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException("Campaign not found"));

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (amount * 100)) // amount in cents
                .setCurrency("usd")
                .setDescription("Donation for " + campaign.getTitle())
                .setReceiptEmail(donor.getEmail())
                .build();

        return PaymentIntent.create(params);
    }

    @Scheduled(cron = "0 0 9 * * ?") // Runs daily at 9 AM
    public void checkExpiredCampaigns() {
        List<EmergencyCampaign> expiredCampaigns = campaignRepository
                .findByIsActiveTrueAndDeadlineBefore(LocalDateTime.now());

        expiredCampaigns.forEach(campaign -> {
            campaign.setActive(false);
            campaignRepository.save(campaign);

            // Notify orphanage owner
            String subject = "Campaign Completed: " + campaign.getTitle();
            String content = String.format(
                    "Your campaign '%s' has reached its deadline.\n" +
                            "Total raised: $%.2f of $%.2f target",
                    campaign.getTitle(),
                    campaign.getCurrentAmount(),
                    campaign.getTargetAmount()
            );

            emailSenderService.sendEmail(
                    campaign.getOrphanage().getEmail(),
                    subject,
                    content
            );
        });
    }

    public List<EmergencyCampaign> getOrphanageCampaigns(User orphanageOwner) {
        Orphanage orphanage = orphanageService.getOrphanageByUser(orphanageOwner);
        return campaignRepository.findByOrphanageAndIsActiveTrue(orphanage);
    }
}
