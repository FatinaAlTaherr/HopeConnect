package com.HopeConnect.HC.repositories.OrphanManagementRepositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.HopeConnect.HC.models.OrphanManagement.Orphan;

public interface OrphanRepository extends JpaRepository<Orphan, Long> {}
