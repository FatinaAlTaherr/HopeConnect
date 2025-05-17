package com.HopeConnect.HC.models.OrphanManagement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Orphan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orphan_id")
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "age")
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "education_status")
    private String educationStatus;

    @Column(name = "school_name")
    private String school;

    @Column(name = "school_grade")
    private Integer grade;

    @Column(name = "academic_performance")
    private String performance;

    @Column(name = "health_condition", columnDefinition = "TEXT")
    private String healthCondition;

    @Column(name = "last_donation_usage", columnDefinition = "TEXT")
    private String lastDonationUsedFor;

    private String photo;

    @Column(name = "recent_photo_url")
    private String recentPhotoURL;

    @ManyToOne
    @JoinColumn(name = "orphanage_id", foreignKey = @ForeignKey(name = "fk_orphan_orphanage"))
    @JsonIgnore
    private Orphanage orphanage;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
