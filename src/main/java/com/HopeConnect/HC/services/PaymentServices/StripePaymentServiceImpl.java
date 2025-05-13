package com.HopeConnect.HC.services.PaymentServices;
import com.HopeConnect.HC.models.Donation.Donation;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class StripePaymentServiceImpl implements PaymentService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public String createPaymentIntent(Donation donation) throws StripeException {
        long amountInCents = (long) (donation.getAmount() * 100);
        long feeInCents = (long) (calculateTransactionFee(donation.getAmount()) * 100);

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents + feeInCents)
                .setCurrency("usd")
                .setDescription("Donation to " + donation.getOrphanage().getName())
                .setReceiptEmail(donation.getDonor().getEmail())
                .putMetadata("donation_id", donation.getId().toString())
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        return paymentIntent.getClientSecret();
    }

    @Override
    public boolean verifyPayment(String paymentIntentId) {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            return "succeeded".equals(paymentIntent.getStatus());
        } catch (StripeException e) {
            return false;
        }
    }

    @Override
    public double calculateTransactionFee(double amount) {
        // 2.9% + $0.30 per transaction
        return (amount * 0.029) + 0.30;
    }
}