package com.HopeConnect.HC.DTO;

import com.HopeConnect.HC.models.Donation.Donation;
import com.HopeConnect.HC.models.Donation.DonationCategory;
import com.HopeConnect.HC.models.Donation.DonationStatus;

import com.HopeConnect.HC.models.Donation.DonationCategory;
import com.HopeConnect.HC.models.Donation.DonationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class DonationSummaryDTO {
    private Double amount;
    private String donorName;
    private String donorEmail;
    private DonationStatus status;
    private DonationCategory category;
    public DonationSummaryDTO(Donation donation) {
        this.amount = donation.getAmount();
        this.status = donation.getStatus();
        this.category = donation.getCategory();
        this.donorName = donation.getDonor().getUsername();
        this.donorEmail = donation.getDonor().getEmail();
    }

    // Getters and setters...
}
