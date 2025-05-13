package com.HopeConnect.HC.services.PaymentServices;

import com.HopeConnect.HC.models.Donation.Donation;
import com.stripe.exception.StripeException;

public interface PaymentService {
    String createPaymentIntent(Donation donation) throws StripeException;
    boolean verifyPayment(String paymentIntentId);
    double calculateTransactionFee(double amount);
}