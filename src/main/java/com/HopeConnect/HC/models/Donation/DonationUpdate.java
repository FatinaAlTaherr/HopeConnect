package com.HopeConnect.HC.models.Donation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "donation_id")
    private Donation donation;

    private String title;
    private String description;
    private LocalDateTime updateDate;
    private String imageUrl;
}