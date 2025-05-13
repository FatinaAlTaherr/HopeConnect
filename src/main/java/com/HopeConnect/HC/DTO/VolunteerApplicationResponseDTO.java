package com.HopeConnect.HC.DTO;

import com.HopeConnect.HC.models.Volunteering.ApplicationStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class VolunteerApplicationResponseDTO {
    private Long applicationId;
    private String volunteerEmail;
    private Set<String> volunteerSkills;
    private String message;
    private LocalDateTime applicationDate;
    private ApplicationStatus status;

    // Service Request Info
    private Long serviceRequestId;
    private String serviceTitle;
    private int volunteersNeeded;
    private int volunteersApproved;
    private int volunteersRemaining;
    private LocalDateTime serviceStartDate;
    private String serviceLocation;
}
