package com.HopeConnect.HC.services.StripeServices;

import com.HopeConnect.HC.DTO.EmergencyDonationRequest;
import com.HopeConnect.HC.DTO.StripeResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmergencyStripeService {

    @Value("${stripe.secretKey}")
    private String secretKey;

    public StripeResponse createDonationSession(EmergencyDonationRequest request) {
        Stripe.apiKey = secretKey;

        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName("Donation for Emergency Campaign ID: " + request.getCampaignId())
                        .build();

        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(request.getCurrency() != null ? request.getCurrency() : "usd")
                        .setUnitAmount(request.getAmount()) // amount in cents
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
                        .setSuccessUrl("http://localhost:8090/emergency/success?session_id={CHECKOUT_SESSION_ID}")
                        .setCancelUrl("http://localhost:8090/emergency/cancel")
                        .putMetadata("campaignId", request.getCampaignId().toString())
                        .putMetadata("donorEmail", request.getDonorEmail() != null ? request.getDonorEmail() : "")
                        .putMetadata("amount", String.valueOf(request.getAmount()))
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
}
