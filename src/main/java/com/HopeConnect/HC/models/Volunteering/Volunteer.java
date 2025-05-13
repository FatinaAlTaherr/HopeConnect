package com.HopeConnect.HC.models.Volunteering;

import com.HopeConnect.HC.models.User.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "email")
    private User user;

    @ElementCollection(targetClass = VolunteerSkill.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "volunteer_skills")
    private Set<VolunteerSkill> skills;

    @ElementCollection(targetClass = AvailabilityDay.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "volunteer_availability")
    private Set<AvailabilityDay> availability;

    private String qualifications;
    private String experience;
    private boolean backgroundCheckVerified;
    private String location;
    private int maxDistance; // in km
}

