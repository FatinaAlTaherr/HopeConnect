package com.HopeConnect.HC.controllers.EmergencyControllers;

import com.HopeConnect.HC.DTO.EmergencyDonationRequest;
import com.HopeConnect.HC.DTO.StripeResponse;
import com.HopeConnect.HC.services.StripeServices.EmergencyStripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emergency")
@RequiredArgsConstructor
public class EmergencyDonationStripeController {

    private final EmergencyStripeService stripeService;

    @PostMapping("/checkout")
    public ResponseEntity<StripeResponse> createStripeSession(@RequestBody EmergencyDonationRequest request) {
        StripeResponse response = stripeService.createDonationSession(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/success")
    public String donationSuccess(@RequestParam("session_id") String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);
            Long amount = session.getAmountTotal() / 100;
            String campaignId = session.getMetadata().get("campaignId");

            return String.format("""
            <html>
            <body>
                <h1>Thank you for your donation!</h1>
                <p>You donated $%d to campaign %s</p>
                <p>Transaction ID: %s</p>
            </body>
            </html>
            """, amount, campaignId, session.getPaymentIntent());
        } catch (StripeException e) {
            return "Donation successful! (Details unavailable)";
        }
    }

    @GetMapping("/cancel")
    public String donationCancelled() {
        return "Donation cancelled.";
    }
}