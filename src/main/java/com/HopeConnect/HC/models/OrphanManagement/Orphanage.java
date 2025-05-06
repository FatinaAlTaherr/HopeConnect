package com.HopeConnect.HC.models.OrphanManagement;

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
public class Orphanage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orphanage_id")
    private Long id;

    @Column(nullable = false)
    private String name;

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
    private List<Orphan> orphans;
}
