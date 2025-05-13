package com.HopeConnect.HC.models.Donation;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "donation_id")
    private Donation donation;

    private String trackingNumber;
    private String carrier;
    private String currentLocation;
    private LocalDateTime estimatedDelivery;
    private LocalDateTime actualDelivery;
}