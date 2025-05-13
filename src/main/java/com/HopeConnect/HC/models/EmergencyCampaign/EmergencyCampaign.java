package com.HopeConnect.HC.models.EmergencyCampaign;

import com.HopeConnect.HC.models.Donation.DonationCategory;
import com.HopeConnect.HC.models.OrphanManagement.Orphanage;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyCampaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orphanage_id")
    private Orphanage orphanage;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private DonationCategory type; // FOOD, MEDICAL, SHELTER, etc.

    private Double targetAmount;
    private Double currentAmount;
    private LocalDateTime deadline;
    private boolean isActive;

    @CreationTimestamp
    private LocalDateTime createdAt;
}