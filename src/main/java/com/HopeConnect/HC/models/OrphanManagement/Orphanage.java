package com.HopeConnect.HC.models.OrphanManagement;

import com.HopeConnect.HC.models.Donation.Review;
import com.HopeConnect.HC.models.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"owner", "reviews", "orphans"})
@Builder
public class Orphanage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orphanage_id")
    private Long id;

    @OneToMany(mappedBy = "orphanage", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> reviews;


    @OneToOne
    @JoinColumn(name = "owner_email", referencedColumnName = "email", nullable = false)
    @Fetch(FetchMode.JOIN)  // Add this
    private User owner;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @Builder.Default
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @Column(length = 1000)
    private String verificationNotes;
    private String location;

    @Column(name = "contact_number", length = 20)
    private String contactNumber;

    @Column(length = 255)
    private String email;

    @Column(name = "verified_status")
    private Boolean verifiedStatus = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "orphanage", cascade = CascadeType.ALL)
    @JsonIgnore 
    private List<Orphan> orphans;
}
