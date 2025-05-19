package com.HopeConnect.HC.DTO;

import com.HopeConnect.HC.models.Partnership.Partner;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PartnerDTO {
    private Long id;
    private String name;
    private String contactEmail;
    private String phone;
    private Partner.PartnerType type;
    private OrphanageSummaryDTO supportedOrphanage;
    private LocalDateTime partnershipStartDate;
    private LocalDateTime updatedAt;
    private boolean verified;
    private String verificationDocumentsUrl;
}