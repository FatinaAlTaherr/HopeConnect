package com.HopeConnect.HC.repositories.EmergencyCampaignRepositories;

import com.HopeConnect.HC.models.EmergencyCampaign.EmergencyCampaign;
import com.HopeConnect.HC.models.OrphanManagement.Orphanage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EmergencyCampaignRepository extends JpaRepository<EmergencyCampaign, Long> {
    List<EmergencyCampaign> findByIsActiveTrueAndDeadlineAfter(LocalDateTime now);
    List<EmergencyCampaign> findByOrphanageAndIsActiveTrue(Orphanage orphanage);

    List<EmergencyCampaign> findByIsActiveTrueAndDeadlineBefore(LocalDateTime now);

}
