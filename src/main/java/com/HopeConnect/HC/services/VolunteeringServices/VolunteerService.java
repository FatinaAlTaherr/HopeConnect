package com.HopeConnect.HC.services.VolunteeringServices;

import com.HopeConnect.HC.DTO.ServiceRequestResponseDTO;
import com.HopeConnect.HC.DTO.VolunteerServiceRequestDTO;
import com.HopeConnect.HC.models.User.User;
import com.HopeConnect.HC.models.OrphanManagement.Orphanage;
import com.HopeConnect.HC.models.Volunteering.*;
import com.HopeConnect.HC.repositories.VolunteerngRepositories.ServiceRequestRepository;
import com.HopeConnect.HC.repositories.VolunteerngRepositories.VolunteerApplicationRepository;
import com.HopeConnect.HC.repositories.VolunteerngRepositories.VolunteerRepository;
import com.HopeConnect.HC.services.ExternalAPI.GeocodingService;
import com.HopeConnect.HC.utils.DistanceCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class VolunteerService {
    private final VolunteerRepository volunteerRepository;
    private final ServiceRequestRepository serviceRequestRepository;
    private final VolunteerApplicationRepository volunteerApplicationRepository;
    private final GeocodingService geocodingService;
    public ServiceRequestResponseDTO toDTO(ServiceRequest request) {
        ServiceRequestResponseDTO dto = new ServiceRequestResponseDTO();
        dto.setId(request.getId());
        dto.setTitle(request.getTitle());
        dto.setDescription(request.getDescription());
        dto.setRequiredSkills(request.getRequiredSkills());
        dto.setStartDateTime(request.getStartDateTime());
        dto.setEndDateTime(request.getEndDateTime());
        dto.setVolunteersNeeded(request.getVolunteersNeeded());
        dto.setUrgent(request.isUrgent());
        dto.setLocation(request.getLocation());
        dto.setStatus(request.getStatus());
        return dto;
    }
    public List<ServiceRequest> findNearbyOpportunities(Volunteer volunteer, int maxDistanceKm) {
        System.out.println(volunteer.getLocation());
        List<ServiceRequest> activeRequests = serviceRequestRepository.findByStatus(RequestStatus.ACTIVE);


        try {
            Map<String, Float> volunteerCoords = geocodingService.getGeocodingData(volunteer.getLocation());
            System.out.println(volunteerCoords);

            return activeRequests.stream()
                    .filter(request -> {
                        System.out.println(request.getLocation());
                        try {
                            System.out.println(request.getLocation());
                            Map<String, Float> requestCoords = geocodingService.getGeocodingData(request.getLocation());
                            double distance = DistanceCalculator.calculateDistance(
                                    volunteerCoords.get("lat"),
                                    volunteerCoords.get("lng"),
                                    requestCoords.get("lat"),
                                    requestCoords.get("lng")
                            );
                            return distance <= maxDistanceKm;
                        } catch (IOException e) {
                            return false;
                        }
                    })
                    .sorted(Comparator.comparing(ServiceRequest::isUrgent).reversed()
                            .thenComparing(ServiceRequest::getStartDateTime))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException("Failed to geocode volunteer location", e);
        }
    }
    public Volunteer registerVolunteer(User user, Set<VolunteerSkill> skills,
                                       Set<AvailabilityDay> availability,
                                       String qualifications, String experience,
                                       String location, int maxDistance) {
        Volunteer volunteer = Volunteer.builder()
                .user(user)
                .skills(skills)
                .availability(availability)
                .qualifications(qualifications)
                .experience(experience)
                .backgroundCheckVerified(false)
                .location(location)
                .maxDistance(maxDistance)
                .build();

        return volunteerRepository.save(volunteer);
    }

    public ServiceRequest createServiceRequest(Orphanage orphanage, String title,
                                               String description, Set<VolunteerSkill> requiredSkills,
                                               LocalDateTime startDateTime, LocalDateTime endDateTime,
                                               int volunteersNeeded, boolean isUrgent, String location) {
        ServiceRequest request = ServiceRequest.builder()
                .orphanage(orphanage)
                .title(title)
                .description(description)
                .requiredSkills(requiredSkills)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .volunteersNeeded(volunteersNeeded)
                .volunteersApplied(0)
                .isUrgent(isUrgent)
                .location(location)
                .status(RequestStatus.PENDING)
                .build();

        return serviceRequestRepository.save(request);
    }
    public List<ServiceRequest> findMatchingOpportunities(Volunteer volunteer, Optional<Integer> distance,
                                                          Optional<String> skills, Optional<String> availability, Optional<Boolean> urgency) {
        List<ServiceRequest> allActiveRequests = serviceRequestRepository.findByStatus(RequestStatus.ACTIVE);

        // Apply distance filter if provided
        if (distance.isPresent()) {
            allActiveRequests = allActiveRequests.stream()
                    .filter(request -> isWithinDistance(volunteer.getLocation(), request.getLocation(), distance.get()))
                    .collect(Collectors.toList());
        }

        // Apply skills filter if provided
        if (skills.isPresent() && !skills.get().isEmpty()) {
            Set<VolunteerSkill> filterSkills = parseSkills(skills.get());
            allActiveRequests = allActiveRequests.stream()
                    .filter(request -> hasMatchingSkills(request.getRequiredSkills(), filterSkills))
                    .collect(Collectors.toList());
        }

        // Apply availability filter if provided
        if (availability.isPresent() && !availability.get().isEmpty()) {
            Set<AvailabilityDay> filterAvailability = parseAvailability(availability.get());
            allActiveRequests = allActiveRequests.stream()
                    .filter(request -> hasMatchingAvailability(volunteer.getAvailability(), filterAvailability))
                    .collect(Collectors.toList());
        }

        // Apply urgency filter if provided
        if (urgency.isPresent()) {
            allActiveRequests = allActiveRequests.stream()
                    .filter(request -> request.isUrgent() == urgency.get())
                    .collect(Collectors.toList());
        }

        // Sort by urgency and start date
        return allActiveRequests.stream()
                .sorted(Comparator.comparing(ServiceRequest::isUrgent).reversed()
                        .thenComparing(ServiceRequest::getStartDateTime))
                .collect(Collectors.toList());
    }

    // Helper methods for filtering
    private boolean isWithinDistance(String volunteerLocation, String requestLocation, int maxDistance) {
        return true; // Implement actual distance calculation logic
    }

    private boolean hasMatchingSkills(Set<VolunteerSkill> requestSkills, Set<VolunteerSkill> filterSkills) {
        return requestSkills.stream().anyMatch(filterSkills::contains);
    }
    private boolean hasMatchingAvailability(Set<AvailabilityDay> volunteerAvailability, Set<AvailabilityDay> requiredAvailability) {
        return !Collections.disjoint(volunteerAvailability, requiredAvailability); // Check availability overlap
    }

    // Parsing helper methods for skills and availability
    private Set<VolunteerSkill> parseSkills(String skillsString) {
        Set<VolunteerSkill> skills = new HashSet<>();
        for (String skill : skillsString.split(",")) {
            skills.add(VolunteerSkill.valueOf(skill.trim().toUpperCase()));
        }
        return skills;
    }

    private Set<AvailabilityDay> parseAvailability(String availabilityString) {
        Set<AvailabilityDay> availability = new HashSet<>();
        for (String day : availabilityString.split(",")) {
            availability.add(AvailabilityDay.valueOf(day.trim().toUpperCase()));
        }
        return availability;
    }


    @Transactional
    public VolunteerApplication applyForOpportunity(Volunteer volunteer, ServiceRequest request, String message) {
        if (volunteerApplicationRepository.existsByVolunteerAndServiceRequest(volunteer, request)) {
            throw new IllegalStateException("You have already applied for this opportunity");
        }

        VolunteerApplication application = VolunteerApplication.builder()
                .volunteer(volunteer)
                .serviceRequest(request)
                .applicationDate(LocalDateTime.now())
                .status(ApplicationStatus.PENDING)
                .message(message)
                .build();

        request.setVolunteersApplied(request.getVolunteersApplied() + 1);
        serviceRequestRepository.save(request);

        return volunteerApplicationRepository.save(application);
    }



    public VolunteerApplication getApplicationById(Long applicationId) {
        return volunteerApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));
    }

    @Transactional
    public VolunteerApplication processApplication(Long applicationId, boolean approve) {
        VolunteerApplication application = volunteerApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        ServiceRequest request = application.getServiceRequest();

        if (approve) {
            application.setStatus(ApplicationStatus.APPROVED);

            // ✅ Decrease volunteersNeeded if greater than 0
            int remaining = request.getVolunteersNeeded();
            if (remaining > 0) {
                request.setVolunteersNeeded(remaining - 1);
            }

            // ✅ Mark as FULFILLED if no more needed
            if (request.getVolunteersNeeded() == 0) {
                request.setStatus(RequestStatus.FULFILLED);
            }

            serviceRequestRepository.save(request);
        } else {
            application.setStatus(ApplicationStatus.REJECTED);
        }

        application.setResponseDate(LocalDateTime.now());
        return volunteerApplicationRepository.save(application);
    }



    private boolean hasMatchingAvailability(Set<AvailabilityDay> volunteerAvailability, LocalDateTime requestDateTime) {
        return volunteerAvailability.contains(requestDateTime.getDayOfWeek());
    }

    public List<VolunteerApplication> getApplicationsForVolunteer(Volunteer volunteer) {
        return volunteerApplicationRepository.findByVolunteer(volunteer);
    }

    public List<VolunteerApplication> getApplicationsForRequest(ServiceRequest request) {
        return volunteerApplicationRepository.findByServiceRequest(request);
    }

    public List<ServiceRequest> getRequestsByOrphanage(Orphanage orphanage) {
        return serviceRequestRepository.findByOrphanage(orphanage);
    }

    public Volunteer getVolunteerByUser(User user) {
        return volunteerRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Volunteer not found for this user"));
    }

    public void cancelApplication(Long applicationId) {
        VolunteerApplication application = volunteerApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Only pending applications can be cancelled");
        }

        application.setStatus(ApplicationStatus.CANCELLED);
        volunteerApplicationRepository.save(application);

        ServiceRequest request = application.getServiceRequest();
        request.setVolunteersApplied(request.getVolunteersApplied() - 1);
        serviceRequestRepository.save(request);
    }

    public void markApplicationAsCompleted(Long applicationId) {
        VolunteerApplication application = volunteerApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        if (application.getStatus() != ApplicationStatus.APPROVED) {
            throw new IllegalStateException("Only approved applications can be marked as completed");
        }

        application.setStatus(ApplicationStatus.COMPLETED);
        volunteerApplicationRepository.save(application);
    }

    public ServiceRequest getServiceRequestById(Long requestId) {
        return serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Service request not found"));
    }


}