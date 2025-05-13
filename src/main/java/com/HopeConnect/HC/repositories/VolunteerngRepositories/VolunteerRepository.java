package com.HopeConnect.HC.repositories.VolunteerngRepositories;

import com.HopeConnect.HC.models.User.User;
import com.HopeConnect.HC.models.Volunteering.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    Optional<Volunteer> findByUser(User user);
}