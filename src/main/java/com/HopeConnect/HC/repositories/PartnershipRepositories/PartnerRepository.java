package com.HopeConnect.HC.repositories.PartnershipRepositories;

import com.HopeConnect.HC.models.Partnership.Partner;
import com.HopeConnect.HC.models.Partnership.Partner.PartnerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    List<Partner> findBySupportedOrphanageId(Long orphanageId);

    List<Partner> findByType(PartnerType type);

    List<Partner> findByVerified(boolean verified);
}
