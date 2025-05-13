package com.HopeConnect.HC.repositories.VolunteerngRepositories;
import com.HopeConnect.HC.models.OrphanManagement.Orphanage;
import com.HopeConnect.HC.models.Volunteering.RequestStatus;
import com.HopeConnect.HC.models.Volunteering.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    List<ServiceRequest> findByStatus(RequestStatus status);
    List<ServiceRequest> findByOrphanage(Orphanage orphanage);
}