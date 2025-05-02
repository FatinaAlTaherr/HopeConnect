package com.HopeConnect.HC.repositories.OrphanManagementRepositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.HopeConnect.HC.models.OrphanManagement.Orphanage;

public interface OrphanageRepository extends JpaRepository<Orphanage, Long> {}
