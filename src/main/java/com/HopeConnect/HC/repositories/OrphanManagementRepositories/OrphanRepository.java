package com.HopeConnect.HC.repositories.OrphanManagementRepositories;

import com.HopeConnect.HC.models.OrphanManagement.Orphanage;
import org.springframework.data.jpa.repository.JpaRepository;

import com.HopeConnect.HC.models.OrphanManagement.Orphan;

import java.util.List;

public interface OrphanRepository extends JpaRepository<Orphan, Long> {
    List<Orphan> findByOrphanage(Orphanage orphanage);

}
