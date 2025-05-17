package com.HopeConnect.HC.controllers.EmergencyControllers;

import com.HopeConnect.HC.services.EmergencyServices.EmergencyService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe/webhook")
@RequiredArgsConstructor
public class StripeWebhookController {

    private final EmergencyService emergencyService;

    @Value("${stripe.webhookSecret}")
    private String endpointSecret;
    @PostMapping
    public ResponseEntity<String> handleStripeEvent(@RequestBody String payload,
                                                    @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            System.out.println("Received webhook event");
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            System.out.println("Event type: " + event.getType());

            if ("checkout.session.completed".equals(event.getType())) {
                Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
                System.out.println("Session: " + session);

                if (session != null) {
                    System.out.println("Session metadata: " + session.getMetadata());
                    String campaignIdStr = session.getMetadata().get("campaignId");
                    Long campaignId = Long.parseLong(campaignIdStr);
                    Long amountCents = session.getAmountTotal();
                    System.out.println("Updating campaign " + campaignId + " with amount " + amountCents);

                    emergencyService.handleSuccessfulDonation(campaignId, amountCents / 100.0);
                }
            }
            return ResponseEntity.ok("Event handled");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Webhook error: " + e.getMessage());
        }
    }
}