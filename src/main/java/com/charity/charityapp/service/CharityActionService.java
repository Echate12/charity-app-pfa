package com.charity.charityapp.service;

import com.charity.charityapp.dto.CharityActionDto;
import com.charity.charityapp.enums.ActionCategory;
import com.charity.charityapp.exceptions.ResourceNotFoundException;
import com.charity.charityapp.model.CharityAction;
import com.charity.charityapp.repository.CharityActionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CharityActionService {

    private final CharityActionRepository charityActionRepository;
    private final ModelMapper modelMapper;

    public List<CharityAction> findAllActions() {
        return charityActionRepository.findAll();
    }

    public CharityAction findActionById(Long id) {
        return charityActionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No action found with id: " + id));
    }

    public List<CharityAction> findByCategory(ActionCategory category) {
        return charityActionRepository.findAllByCategory(ActionCategory.valueOf(category.name()));
    }

    public List<CharityAction> searchActions(String keyword, ActionCategory category) {
        if (category != null) {
            return charityActionRepository.findAllByTitleContainingIgnoreCaseAndCategory(keyword, category);
        }
        return charityActionRepository.findAllByTitleContainingIgnoreCase(keyword);
    }

    public CharityAction createAction(CharityActionDto actionDto) {

        CharityAction action = modelMapper.map(actionDto, CharityAction.class);
        action.setOrganization(organization);
        action.setAmountRaised(0.0); // Initialize with 0 donations
        action.setCreatedAt(LocalDateTime.now());
        return charityActionRepository.save(action);
    }

    public void updateAction(Long id, CharityActionDto actionDto) {
        CharityAction action = findActionById(id);
        action.setCategory(actionDto.getCategory());
        action.setTitle(actionDto.getTitle());
        action.setDescription(actionDto.getDescription());
        action.setAmountRaised(actionDto.getAmountRaised());
        action.setFundingGoal(actionDto.getFundingGoal());
        charityActionRepository.save(action);
    }

    public void deleteAction(Long id) {
        charityActionRepository.deleteById(id);
    }
}
