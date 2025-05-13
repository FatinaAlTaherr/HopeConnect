package com.HopeConnect.HC.repositories.DonationRepositories;

import com.HopeConnect.HC.models.Donation.Donation;
import com.HopeConnect.HC.models.Donation.DonationUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonationUpdateRepository extends JpaRepository<DonationUpdate, Long> {
    List<DonationUpdate> findByDonation(Donation donation);
}