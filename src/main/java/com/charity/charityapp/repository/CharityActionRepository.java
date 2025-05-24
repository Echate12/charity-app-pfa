// src/main/java/com/charity/charityapp/repository/CharityActionRepository.java
package com.charity.charityapp.repository;

import com.charity.charityapp.model.CharityAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CharityActionRepository extends JpaRepository<CharityAction, Long> {
    List<CharityAction> findByTitleContainingIgnoreCase(String keyword);
}
