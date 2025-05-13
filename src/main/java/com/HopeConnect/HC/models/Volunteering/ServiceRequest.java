package com.HopeConnect.HC.models.Volunteering;


import com.HopeConnect.HC.models.OrphanManagement.Orphanage;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orphanage_id")
    private Orphanage orphanage;

    private String title;
    private String description;

    @ElementCollection(targetClass = VolunteerSkill.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "request_required_skills")
    private Set<VolunteerSkill> requiredSkills;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int volunteersNeeded;
    private int volunteersApplied;
    private boolean isUrgent;
    private String location;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}
