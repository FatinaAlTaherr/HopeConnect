package com.HopeConnect.HC.services.OrphanManagementServices;

import com.HopeConnect.HC.models.OrphanManagement.Orphan;
import com.HopeConnect.HC.models.OrphanManagement.Orphanage;
import com.HopeConnect.HC.repositories.OrphanManagementRepositories.OrphanRepository;
import com.HopeConnect.HC.repositories.OrphanManagementRepositories.OrphanageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrphanageService {

    private OrphanageRepository orphanageRepository;
    private OrphanRepository orphanRepository;

    // Orphanage Methods
    public Orphanage saveOrphanage(Orphanage orphanage) {
        return orphanageRepository.save(orphanage);
    }

    public List<Orphanage> getAllOrphanages() {
        return orphanageRepository.findAll();
    }

    public Orphanage getOrphanageById(Long id) {
        return orphanageRepository.findById(id).orElse(null);
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
