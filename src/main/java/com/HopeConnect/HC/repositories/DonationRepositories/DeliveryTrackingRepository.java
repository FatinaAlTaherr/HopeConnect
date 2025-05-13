package com.HopeConnect.HC.repositories.DonationRepositories;

import com.HopeConnect.HC.models.Donation.DeliveryTracking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryTrackingRepository extends JpaRepository<DeliveryTracking, Long> {
}