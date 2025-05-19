package com.HopeConnect.HC.models.Partnership;

import com.HopeConnect.HC.models.OrphanManagement.Orphanage;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Partner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String contactEmail;
    private String phone;

    @Enumerated(EnumType.STRING)
    private PartnerType type; // NGO, CHARITY, HUMANITARIAN_ORG

    @ManyToOne
    @JoinColumn(name = "orphanage_id")
    private Orphanage supportedOrphanage;

    @CreationTimestamp
    private LocalDateTime partnershipStartDate;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean verified;
    private String verificationDocumentsUrl;

    public enum PartnerType {
        NGO,
        CHARITY,
        HUMANITARIAN_ORG,
        CORPORATE_SPONSOR
    }
}