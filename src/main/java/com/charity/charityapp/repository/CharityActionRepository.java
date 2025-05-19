package com.charity.charityapp.repository;

import com.charity.charityapp.enums.ActionCategory;
import com.charity.charityapp.model.CharityAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharityActionRepository extends JpaRepository<CharityAction, Long> {

    List<CharityAction> findAllByCategory(ActionCategory category);

    List<CharityAction> findAllByTitleContainingIgnoreCase(String keyword);

    List<CharityAction> findAllByTitleContainingIgnoreCaseAndCategory(String keyword,ActionCategory category);
}
