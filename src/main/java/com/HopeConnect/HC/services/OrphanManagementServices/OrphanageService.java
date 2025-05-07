package com.HopeConnect.HC.services.OrphanManagementServices;

import com.HopeConnect.HC.models.OrphanManagement.Orphan;
import com.HopeConnect.HC.models.OrphanManagement.Orphanage;
import com.HopeConnect.HC.repositories.OrphanManagementRepositories.OrphanRepository;
import com.HopeConnect.HC.repositories.OrphanManagementRepositories.OrphanageRepository;
import com.HopeConnect.HC.services.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrphanageService {

    private final OrphanageRepository orphanageRepository;
    private final OrphanRepository orphanRepository;
    private final EmailSenderService emailSenderService;

    // Orphanage Methods
    public Orphanage saveOrphanage(Orphanage orphanage) {
        Orphanage savedOrphanage = orphanageRepository.save(orphanage);

        // Send email verification
        String subject = "Verify Your Orphanage Registration";
        String body = "Dear " + orphanage.getName() + ",\n\n" +
                "Please click the link below to verify your orphanage registration:\n" +
                "http://localhost:8090/HopeConnect/api/orphanages/verify/" + savedOrphanage.getId() + "\n\n" +
                "Best regards,\n" +
                "HopeConnect Team";

        emailSenderService.sendEmail(savedOrphanage.getEmail(), subject, body);

        return savedOrphanage;
    }

    public void deleteOrphanage(Long id) {
        orphanageRepository.deleteById(id);
    }


    public List<Orphanage> getAllOrphanages() {
        return orphanageRepository.findAll();
    }

    public Orphanage getOrphanageById(Long id) {
        return orphanageRepository.findById(id).orElse(null);
    }

    public Orphanage verifyOrphanage(Long id) {
        Orphanage orphanage = orphanageRepository.findById(id).orElse(null);
        if (orphanage != null) {
            orphanage.setVerifiedStatus(true); 
            orphanageRepository.save(orphanage); 
        }
        return orphanage;
    }

    // Orphan Methods
    public Orphan saveOrphan(Orphan orphan) {
        return orphanRepository.save(orphan);
    }

    public List<Orphan> getAllOrphans() {
        return orphanRepository.findAll();
    }

    public Orphan getOrphanById(Long id) {
        return orphanRepository.findById(id).orElse(null);
    }

    public Orphan updateOrphan(Long id, Orphan updatedOrphan) {
        return orphanRepository.findById(id).map(o -> {
            o.setFullName(updatedOrphan.getFullName());
            o.setBirthDate(updatedOrphan.getBirthDate());
            o.setGender(updatedOrphan.getGender());
            o.setEducationStatus(updatedOrphan.getEducationStatus());
            o.setHealthCondition(updatedOrphan.getHealthCondition());
            o.setPhoto(updatedOrphan.getPhoto());
            o.setOrphanage(updatedOrphan.getOrphanage());
            return orphanRepository.save(o);
        }).orElse(null);
    }

    public void deleteOrphan(Long id) {
        orphanRepository.deleteById(id);
    }
}
