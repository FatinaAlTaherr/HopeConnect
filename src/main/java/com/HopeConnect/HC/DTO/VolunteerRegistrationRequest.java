package com.HopeConnect.HC.DTO;


import com.HopeConnect.HC.models.Volunteering.AvailabilityDay;
import com.HopeConnect.HC.models.Volunteering.VolunteerSkill;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class VolunteerRegistrationRequest {
    private Set<VolunteerSkill> skills;
    private Set<AvailabilityDay> availability;
    private String qualifications;
    private String experience;
    private String location;
    private int maxDistance;
}
