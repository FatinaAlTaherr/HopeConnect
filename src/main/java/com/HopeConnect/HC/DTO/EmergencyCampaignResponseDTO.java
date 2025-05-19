package com.HopeConnect.HC.DTO;

import com.HopeConnect.HC.models.EmergencyCampaign.EmergencyCampaign;
import com.HopeConnect.HC.models.OrphanManagement.Orphanage;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EmergencyCampaignResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String type;
    private Double targetAmount;
    private Double currentAmount;
    private LocalDateTime deadline;
    private boolean active;
    private LocalDateTime createdAt;

    // Orphanage summary (no owner)
    private OrphanageSummary orphanage;

    @Data
    @AllArgsConstructor
    public static class OrphanageSummary {
        private Long id;
        private String name;
        private String location;
        private String contactNumber;
        private String email;
        private boolean verifiedStatus;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    public static EmergencyCampaignResponseDTO fromCampaign(EmergencyCampaign campaign) {
        Orphanage o = campaign.getOrphanage();
        OrphanageSummary orphanageSummary = new OrphanageSummary(
                o.getId(),
                o.getName(),
                o.getLocation(),
                o.getContactNumber(),
                o.getEmail(),
                o.getVerifiedStatus(),
                o.getCreatedAt(),
                o.getUpdatedAt()
        );

        return new EmergencyCampaignResponseDTO(
                campaign.getId(),
                campaign.getTitle(),
                campaign.getDescription(),
                campaign.getType().name(),
                campaign.getTargetAmount(),
                campaign.getCurrentAmount(),
                campaign.getDeadline(),
                campaign.isActive(),
                campaign.getCreatedAt(),
                orphanageSummary
        );
    }
}
