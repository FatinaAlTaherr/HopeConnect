package com.HopeConnect.HC.repositories.SponsorshipRepositories;

import com.HopeConnect.HC.models.Sponsorship.Sponsorship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SponsorshipRepository extends JpaRepository<Sponsorship, Long> {
    List<Sponsorship> findByUserEmail(String email);
    List<Sponsorship> findByOrphanId(Long orphanId);
}

