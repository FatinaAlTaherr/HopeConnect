package com.HopeConnect.HC.models.Donation;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @ElementCollection
    @CollectionTable(name = "delivery_checkpoints", joinColumns = @JoinColumn(name = "tracking_id"))
    private List<DeliveryCheckpoint> checkpoints;

    private LocalDateTime estimatedDelivery;
    private LocalDateTime actualDelivery;
    private String location;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DeliveryCheckpoint {
        private String location;
        private LocalDateTime timestamp;
        private String status;
    }

    public enum DeliveryStatus {
        PICKED_UP,
        IN_TRANSIT,
        AT_WAREHOUSE,
        OUT_FOR_DELIVERY,
        DELIVERED,
        RETURNED
    }
}