package com.HopeConnect.HC.repositories.DonationRepositories;

import com.HopeConnect.HC.models.Donation.Review;
import com.HopeConnect.HC.models.OrphanManagement.Orphanage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByOrphanage(Orphanage orphanage);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.orphanage = :orphanage")
    Double findAverageRatingByOrphanage(Orphanage orphanage);
}