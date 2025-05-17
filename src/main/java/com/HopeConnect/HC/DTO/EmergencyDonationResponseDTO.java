package com.HopeConnect.HC.DTO;

import com.HopeConnect.HC.models.EmergencyCampaign.EmergencyDonation;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EmergencyDonationResponseDTO {
    private Long id;
    private String donorEmail;
    private String campaignTitle;
    private String orphanageName;
    private Double amount;
    private LocalDateTime donationDate;

    public static EmergencyDonationResponseDTO fromDonation(EmergencyDonation donation) {
        return EmergencyDonationResponseDTO.builder()
                .id(donation.getId())
                .donorEmail(donation.getDonor().getEmail())
                .campaignTitle(donation.getCampaign().getTitle())
                .orphanageName(donation.getCampaign().getOrphanage().getName())
                .amount(donation.getAmount())
                .donationDate(donation.getDonationDate())
                .build();
    }
}
