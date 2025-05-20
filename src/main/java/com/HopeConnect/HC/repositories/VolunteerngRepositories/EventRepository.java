package com.HopeConnect.HC.repositories.VolunteerngRepositories;

import com.HopeConnect.HC.models.Volunteering.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Custom finder method to get an event by its name
    Event findByName(String name);
}
