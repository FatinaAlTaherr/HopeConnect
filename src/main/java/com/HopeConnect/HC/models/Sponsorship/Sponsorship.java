package com.HopeConnect.HC.models.Sponsorship;

import com.HopeConnect.HC.models.User.User;
import com.HopeConnect.HC.models.OrphanManagement.Orphan;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sponsorship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "orphan_id", nullable = false)
    private Orphan orphan;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private SponsorshipStatus status;
}