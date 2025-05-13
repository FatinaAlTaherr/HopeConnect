package com.HopeConnect.HC.models.Donation;

import com.HopeConnect.HC.models.OrphanManagement.Orphanage;
import com.HopeConnect.HC.models.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "donor_email", referencedColumnName = "email")
    private User donor;

    @ManyToOne
    @JoinColumn(name = "orphanage_id")
    private Orphanage orphanage;

    @Enumerated(EnumType.STRING)
    private DonationType type;

    @Enumerated(EnumType.STRING)
    private DonationCategory category;

    private Double amount;

    private String description;

    @Enumerated(EnumType.STRING)
    private DonationStatus status;

    // Add this new field for payment intent
    private String paymentIntent;

    @OneToMany(mappedBy = "donation", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DonationItem> items;

    @OneToMany(mappedBy = "donation", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DonationUpdate> updates;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "donation", cascade = CascadeType.ALL)
    @JsonIgnore
    private DeliveryTracking deliveryTracking;


}