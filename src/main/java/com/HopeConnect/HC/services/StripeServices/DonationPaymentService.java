package com.HopeConnect.HC.services.StripeServices;

import com.HopeConnect.HC.DTO.StripeResponse;
import com.HopeConnect.HC.models.Donation.Donation;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DonationPaymentService {

    @Value("${stripe.secretKey}")
    private String secretKey;


    public StripeResponse createDonationSession(Donation donation) {
        Stripe.apiKey = secretKey;

        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName("Donation to " + donation.getOrphanage().getName())
                        .setDescription(donation.getDescription() != null ?
                                donation.getDescription() : "General donation")
                        .build();

        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("usd") // or make configurable
                        .setUnitAmount((long)(donation.getAmount() * 100)) // amount in cents
                        .setProductData(productData)
                        .build();

        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(priceData)
                        .build();

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:8090/HopeConnect/api/donations/success?session_id={CHECKOUT_SESSION_ID}")
                        .setCancelUrl("http://localhost:8090/HopeConnect/api/donations/cancel")
                        .putMetadata("donationId", donation.getId().toString())
                        .putMetadata("donorEmail", donation.getDonor().getEmail())
                        .putMetadata("orphanageId", donation.getOrphanage().getId().toString())
                        .putMetadata("amount", String.valueOf(donation.getAmount()))
                        .putMetadata("transactionFee", String.valueOf(donation.getTransactionFee()))
                        .putMetadata("netAmount", String.valueOf(donation.getNetAmount()))
                        .addLineItem(lineItem)
                        .build();

        try {
            Session session = Session.create(params);
            return StripeResponse.builder()
                    .status("SUCCESS")
                    .message("Stripe session created")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();
        } catch (StripeException e) {
            e.printStackTrace();
            return StripeResponse.builder()
                    .status("FAILED")
                    .message("Stripe session creation failed: " + e.getMessage())
                    .build();
        }
    }

    public void confirmSuccessfulPayment(String sessionId) throws StripeException {
        Stripe.apiKey = secretKey;
        Session session = Session.retrieve(sessionId);

        if (!"paid".equals(session.getPaymentStatus())) {
            throw new RuntimeException("Payment not successful");
        }
    }
}