package com.HopeConnect.HC.models.Donation;

import com.HopeConnect.HC.models.OrphanManagement.Orphanage;
import com.HopeConnect.HC.models.User.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "donor_email", referencedColumnName = "email")
    private User donor;

    @ManyToOne
    @JoinColumn(name = "orphanage_id")
    private Orphanage orphanage;

    private Integer rating;
    private String comment;
    private LocalDateTime reviewDate;

    @PrePersist
    public void prePersist() {
        if (reviewDate == null) {
            reviewDate = LocalDateTime.now();
        }
    }

}