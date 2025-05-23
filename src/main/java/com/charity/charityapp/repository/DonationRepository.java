package com.charity.charityapp.repository;

import com.charity.charityapp.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByCharityAction_Id(Long actionId);
}
