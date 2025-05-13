package com.HopeConnect.HC.repositories.VolunteerngRepositories;

import com.HopeConnect.HC.models.Volunteering.ApplicationStatus;
import com.HopeConnect.HC.models.Volunteering.ServiceRequest;
import com.HopeConnect.HC.models.Volunteering.Volunteer;
import com.HopeConnect.HC.models.Volunteering.VolunteerApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VolunteerApplicationRepository extends JpaRepository<VolunteerApplication, Long> {
    List<VolunteerApplication> findByVolunteer(Volunteer volunteer);
    List<VolunteerApplication> findByServiceRequest(ServiceRequest serviceRequest);
    boolean existsByVolunteerAndServiceRequest(Volunteer volunteer, ServiceRequest serviceRequest);
    long countByServiceRequestAndStatus(ServiceRequest serviceRequest, ApplicationStatus status);
}