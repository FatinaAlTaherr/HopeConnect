package com.HopeConnect.HC.DTO;

import com.HopeConnect.HC.models.Volunteering.RequestStatus;
import com.HopeConnect.HC.models.Volunteering.VolunteerSkill;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ServiceRequestResponseDTO {
    private Long id;
    private String title;
    private String description;
    private Set<VolunteerSkill> requiredSkills;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int volunteersNeeded;
    private int volunteersApplied;
    private String location;
    private RequestStatus status;
    private boolean urgent;
}
