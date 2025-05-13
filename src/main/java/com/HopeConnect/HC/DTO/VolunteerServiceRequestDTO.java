
package com.HopeConnect.HC.DTO;

        import com.HopeConnect.HC.models.Volunteering.ServiceRequest;
        import com.HopeConnect.HC.models.Volunteering.VolunteerSkill;
        import lombok.AllArgsConstructor;
        import lombok.Data;

        import java.time.LocalDateTime;
        import java.util.Set;

@Data
@AllArgsConstructor
public class VolunteerServiceRequestDTO {
    private Long id;
    private String orphanageName;
    private String orphanageLocation;
    private String title;
    private String description;
    private Set<VolunteerSkill> requiredSkills;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int volunteersNeeded;
    private int volunteersApplied;
    private String location;
    private boolean urgent;

    // Constructor to create the DTO from ServiceRequest
    public static VolunteerServiceRequestDTO fromServiceRequest(ServiceRequest serviceRequest) {
        return new VolunteerServiceRequestDTO(
                serviceRequest.getId(),
                serviceRequest.getOrphanage().getName(),
                serviceRequest.getOrphanage().getLocation(),
                serviceRequest.getTitle(),
                serviceRequest.getDescription(),
                serviceRequest.getRequiredSkills(),
                serviceRequest.getStartDateTime(),
                serviceRequest.getEndDateTime(),
                serviceRequest.getVolunteersNeeded(),
                serviceRequest.getVolunteersApplied(),
                serviceRequest.getLocation(),
                serviceRequest.isUrgent()
        );
    }
}
