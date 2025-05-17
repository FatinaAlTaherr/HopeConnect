package com.HopeConnect.HC.controllers.VolunteeringControllers;
import com.HopeConnect.HC.DTO.ServiceRequestResponseDTO;
import com.HopeConnect.HC.DTO.VolunteerRegistrationRequest;
import com.HopeConnect.HC.DTO.VolunteerServiceRequestDTO;
import com.HopeConnect.HC.models.User.User;
import com.HopeConnect.HC.models.Volunteering.ServiceRequest;
import com.HopeConnect.HC.models.Volunteering.Volunteer;
import com.HopeConnect.HC.models.Volunteering.VolunteerApplication;
import com.HopeConnect.HC.services.VolunteeringServices.VolunteerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/volunteer")
@PreAuthorize("hasAuthority('VOLUNTEER')")
@RequiredArgsConstructor
public class VolunteerController {
    private final VolunteerService volunteerService;


    @GetMapping("/nearby")
    public List<ServiceRequestResponseDTO> getNearbyOpportunities(
            @AuthenticationPrincipal User user,
            @RequestParam int maxDistanceKm) {

        Volunteer volunteer = volunteerService.getVolunteerByUser(user);
        List<ServiceRequest> nearbyRequests = volunteerService.findNearbyOpportunities(volunteer, maxDistanceKm);

        return nearbyRequests.stream()
                .map(volunteerService::toDTO)
                .collect(Collectors.toList());
    }
    @PostMapping("/register")
    public Volunteer registerVolunteer(
            @AuthenticationPrincipal User user,
            @RequestBody VolunteerRegistrationRequest request) {

        return volunteerService.registerVolunteer(
                user,
                request.getSkills(),
                request.getAvailability(),
                request.getQualifications(),
                request.getExperience(),
                request.getLocation(),
                request.getMaxDistance()
        );
    }
    @GetMapping("/opportunities")
    public List<VolunteerServiceRequestDTO> getMatchingOpportunities(@AuthenticationPrincipal User user,
                                                                     @RequestParam Optional<Integer> distance,
                                                                     @RequestParam Optional<String> skills,
                                                                     @RequestParam Optional<String> availability,
                                                                     @RequestParam Optional<Boolean> urgency) {
        // Retrieve the volunteer object
        Volunteer volunteer = volunteerService.getVolunteerByUser(user);

        // Get the matching service requests
        List<ServiceRequest> matchingRequests = volunteerService.findMatchingOpportunities(volunteer, distance, skills, availability, urgency);

        // Convert the ServiceRequest objects into ServiceRequestDTO
        return matchingRequests.stream()
                .map(VolunteerServiceRequestDTO::fromServiceRequest)
                .collect(Collectors.toList());
    }

    @PostMapping("/apply/{requestId}")
    public VolunteerApplication applyForOpportunity(
            @AuthenticationPrincipal User user,
            @PathVariable Long requestId,
            @RequestBody String message) {

        Volunteer volunteer = volunteerService.getVolunteerByUser(user);
        ServiceRequest request = volunteerService.getServiceRequestById(requestId);

        return volunteerService.applyForOpportunity(volunteer, request, message);
    }

    @GetMapping("/applications")
    public List<VolunteerApplication> getMyApplications(@AuthenticationPrincipal User user) {
        Volunteer volunteer = volunteerService.getVolunteerByUser(user);
        return volunteerService.getApplicationsForVolunteer(volunteer);
    }

    @PostMapping("/applications/{applicationId}/cancel")
    public void cancelApplication(
            @AuthenticationPrincipal User user,
            @PathVariable Long applicationId) {

        volunteerService.cancelApplication(applicationId);
    }
}