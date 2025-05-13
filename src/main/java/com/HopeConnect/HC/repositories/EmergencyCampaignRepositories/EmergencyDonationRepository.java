package com.HopeConnect.HC.repositories.EmergencyCampaignRepositories;

import com.HopeConnect.HC.models.EmergencyCampaign.EmergencyCampaign;
import com.HopeConnect.HC.models.EmergencyCampaign.EmergencyDonation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmergencyDonationRepository extends JpaRepository<EmergencyDonation, Long> {
    List<EmergencyDonation> findByCampaign(EmergencyCampaign campaign);
}