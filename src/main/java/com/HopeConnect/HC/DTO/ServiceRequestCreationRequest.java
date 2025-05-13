package com.HopeConnect.HC.DTO;

import com.HopeConnect.HC.models.Volunteering.VolunteerSkill;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ServiceRequestCreationRequest {
    private String title;
    private String description;
    private Set<VolunteerSkill> requiredSkills;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int volunteersNeeded;
    private boolean isUrgent;
    private String location;
}