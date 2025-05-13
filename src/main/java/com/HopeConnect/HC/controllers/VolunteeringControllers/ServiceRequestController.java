package com.HopeConnect.HC.controllers.VolunteeringControllers;

import com.HopeConnect.HC.DTO.ServiceRequestCreationRequest;
import com.HopeConnect.HC.DTO.ServiceRequestResponseDTO;
import com.HopeConnect.HC.DTO.VolunteerApplicationResponseDTO;
import com.HopeConnect.HC.models.OrphanManagement.Orphanage;
import com.HopeConnect.HC.models.User.User;
import com.HopeConnect.HC.models.Volunteering.ApplicationStatus;
import com.HopeConnect.HC.models.Volunteering.ServiceRequest;
import com.HopeConnect.HC.models.Volunteering.VolunteerApplication;
import com.HopeConnect.HC.services.OrphanManagementServices.OrphanageService;
import com.HopeConnect.HC.services.VolunteeringServices.VolunteerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orphanage/service-requests")
@PreAuthorize("hasAuthority('ORPHANAGE_OWNER')")
@RequiredArgsConstructor
public class ServiceRequestController {
    private final VolunteerService volunteerService;
    private final OrphanageService orphanageService;

    @PostMapping
    public ServiceRequest createServiceRequest(
            @AuthenticationPrincipal User user,
            @RequestBody ServiceRequestCreationRequest request) {

        Orphanage orphanage = orphanageService.getOrphanageByUser(user);

        return volunteerService.createServiceRequest(
                orphanage,
                request.getTitle(),
                request.getDescription(),
                request.getRequiredSkills(),
                request.getStartDateTime(),
                request.getEndDateTime(),
                request.getVolunteersNeeded(),
                request.isUrgent(),
                request.getLocation()
        );
    }

    @GetMapping
    public List<ServiceRequestResponseDTO> getMyOrphanageRequests(@AuthenticationPrincipal User user) {
        Orphanage orphanage = orphanageService.getOrphanageByUser(user);
        List<ServiceRequest> requests = volunteerService.getRequestsByOrphanage(orphanage);

        return requests.stream().map(this::mapToResponseDTO).toList();
    }

    private ServiceRequestResponseDTO mapToResponseDTO(ServiceRequest request) {
        ServiceRequestResponseDTO dto = new ServiceRequestResponseDTO();
        dto.setId(request.getId());
        dto.setTitle(request.getTitle());
        dto.setDescription(request.getDescription());
        dto.setRequiredSkills(request.getRequiredSkills());
        dto.setStartDateTime(request.getStartDateTime());
        dto.setEndDateTime(request.getEndDateTime());
        dto.setVolunteersNeeded(request.getVolunteersNeeded());
        dto.setVolunteersApplied(request.getVolunteersApplied());
        dto.setLocation(request.getLocation());
        dto.setStatus(request.getStatus());
        dto.setUrgent(request.isUrgent());
        return dto;
    }


    @GetMapping("/{requestId}/applications")
    public List<VolunteerApplication> getApplicationsForRequest(
            @AuthenticationPrincipal User user,
            @PathVariable Long requestId) {

        ServiceRequest request = volunteerService.getServiceRequestById(requestId);

        Orphanage orphanage = orphanageService.getOrphanageByUser(user);

        if (!request.getOrphanage().equals(orphanage)) {
            throw new IllegalArgumentException("You don't have permission to view these applications");
        }

        return volunteerService.getApplicationsForRequest(request);
    }
    @PostMapping("/applications/{applicationId}/process")
    public VolunteerApplicationResponseDTO processApplication(
            @AuthenticationPrincipal User user,
            @PathVariable Long applicationId,
            @RequestBody boolean approve) {

        VolunteerApplication application = volunteerService.getApplicationById(applicationId);
        Orphanage orphanage = orphanageService.getOrphanageByUser(user);

        if (!application.getServiceRequest().getOrphanage().equals(orphanage)) {
            throw new SecurityException("You are not authorized to process this application");
        }

        VolunteerApplication updatedApplication = volunteerService.processApplication(applicationId, approve);

        return mapToResponseDTO(updatedApplication);
    }


    private VolunteerApplicationResponseDTO mapToResponseDTO(VolunteerApplication application) {
        VolunteerApplicationResponseDTO dto = new VolunteerApplicationResponseDTO();

        dto.setApplicationId(application.getId());
        dto.setVolunteerEmail(application.getVolunteer().getUser().getEmail());
        dto.setVolunteerSkills(
                application.getVolunteer().getSkills().stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet())
        );
        dto.setMessage(application.getMessage());
        dto.setApplicationDate(application.getApplicationDate());
        dto.setStatus(application.getStatus());

        ServiceRequest request = application.getServiceRequest();
        dto.setServiceRequestId(request.getId());
        dto.setServiceTitle(request.getTitle());
        dto.setVolunteersNeeded(request.getVolunteersNeeded());

        int approved = (int) volunteerService
                .getApplicationsForRequest(request).stream()
                .filter(a -> a.getStatus() == ApplicationStatus.APPROVED)
                .count();

        dto.setVolunteersApproved(approved);
        dto.setVolunteersRemaining(Math.max(0, request.getVolunteersNeeded() - approved));
        dto.setServiceStartDate(request.getStartDateTime());
        dto.setServiceLocation(request.getLocation());

        return dto;
    }


}