package com.HopeConnect.HC.repositories.DonationRepositories;

import com.HopeConnect.HC.models.Donation.Donation;
import com.HopeConnect.HC.models.Donation.DonationCategory;
import com.HopeConnect.HC.models.Donation.DonationStatus;
import com.HopeConnect.HC.models.OrphanManagement.Orphanage;
import com.HopeConnect.HC.models.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByDonor(User donor);
    List<Donation> findByOrphanage(Orphanage orphanage);
    List<Donation> findByDonorAndStatus(User donor, DonationStatus status);
    List<Donation> findByOrphanageAndStatus(Orphanage orphanage, DonationStatus status);
    Optional<Donation> findByPaymentIntent(String paymentIntent);

}