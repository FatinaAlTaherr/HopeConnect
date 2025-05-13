package com.HopeConnect.HC.repositories.OrphanManagementRepositories;
import com.HopeConnect.HC.models.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.HopeConnect.HC.models.OrphanManagement.Orphanage;

import java.util.List;
import java.util.Optional;

public interface OrphanageRepository extends JpaRepository<Orphanage, Long> {
    Optional<Orphanage> findByEmail(String email);
    List<Orphanage> findAll();
    Optional<Orphanage> findById(Long id);
    void deleteById(Long id);
    Optional<Orphanage> findByOwner(User owner);

}