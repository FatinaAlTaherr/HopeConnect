package com.HopeConnect.HC.repositories;

import com.HopeConnect.HC.models.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    void deleteByEmail(String email);

}

