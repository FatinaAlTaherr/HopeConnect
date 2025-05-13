package com.HopeConnect.HC.DTO;

import com.HopeConnect.HC.models.Donation.DonationCategory;
import com.HopeConnect.HC.models.Donation.DonationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class DonorSummaryDTO {
    private double totalDonated;
    private long totalDonations;
    private long completedDonations;
    private List<RecentDonationDTO> recentDonations;

    @Data
    @AllArgsConstructor
    public static class RecentDonationDTO {
        private Long id;
        private DonationCategory category;
        private double amount;
        private DonationStatus status;
        private LocalDateTime createdAt;
    }
}
