package com.HopeConnect.HC.DTO;

import com.HopeConnect.HC.models.Donation.DonationCategory;
import com.HopeConnect.HC.models.Donation.DonationStatus;
import com.HopeConnect.HC.models.Donation.DonationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationResponseDTO {
    private Long id;
    private DonorInfo donor;
    private String orphanageName;
    private DonationType type;
    private DonationCategory category;
    private Double amount;
    private String description;
    private DonationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DonorInfo {
        private String email;
        private String phoneNumber;
    }
}
