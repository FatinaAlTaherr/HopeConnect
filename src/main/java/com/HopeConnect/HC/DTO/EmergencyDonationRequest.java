package com.HopeConnect.HC.DTO;

import lombok.Data;

@Data
public class EmergencyDonationRequest {
    private Long campaignId;
    private String donorEmail;
    private Long amount;  // in cents (Stripe expects smallest currency unit)
    private String currency;
}