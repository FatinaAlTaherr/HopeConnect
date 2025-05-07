package com.HopeConnect.HC.services.SponsershipServices;

import com.HopeConnect.HC.DTO.SponsorshipRequest;
import com.HopeConnect.HC.models.OrphanManagement.Orphan;
import com.HopeConnect.HC.models.Sponsorship.Sponsorship;
import com.HopeConnect.HC.models.Sponsorship.SponsorshipStatus;
import com.HopeConnect.HC.models.User.User;
import com.HopeConnect.HC.repositories.OrphanManagementRepositories.OrphanRepository;
import com.HopeConnect.HC.repositories.SponsorshipRepositories.SponsorshipRepository;
import com.HopeConnect.HC.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SponsorshipService {

    private final SponsorshipRepository sponsorshipRepo;
    private final UserRepository userRepo;
    private final OrphanRepository orphanRepo;
    public Sponsorship createSponsorship(SponsorshipRequest request) {
        User user = userRepo.findByEmail(request.getUserEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + request.getUserEmail()));

        Orphan orphan = orphanRepo.findById(request.getOrphanId())
                .orElseThrow(() -> new RuntimeException("Orphan not found"));

        Sponsorship sponsorship = Sponsorship.builder()
                .user(user)
                .orphan(orphan)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(SponsorshipStatus.ACTIVE)
                .build();

        return sponsorshipRepo.save(sponsorship);
    }

    public List<Sponsorship> getAll() {
        return sponsorshipRepo.findAll();
    }

    public List<Sponsorship> getByUser(String email) {
        return sponsorshipRepo.findByUserEmail(email);
    }

    public List<Sponsorship> getByOrphan(Long orphanId) {
        return sponsorshipRepo.findByOrphanId(orphanId);
    }

    public void endSponsorship(Long sponsorshipId) {
        Sponsorship s = sponsorshipRepo.findById(sponsorshipId)
                .orElseThrow(() -> new RuntimeException("Sponsorship not found"));
        s.setStatus(SponsorshipStatus.COMPLETED);
        sponsorshipRepo.save(s);
    }


    public void deleteSponsorship(Long sponsorshipId) {
        if (!sponsorshipRepo.existsById(sponsorshipId)) {
            throw new RuntimeException("Sponsorship not found with ID: " + sponsorshipId);
        }
        sponsorshipRepo.deleteById(sponsorshipId);
    }
}