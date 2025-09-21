package com.example.tabi.quest.actions.locationpuzzleaction.service;

import com.example.tabi.quest.actions.hint.service.HintService;
import com.example.tabi.quest.actions.hint.vo.HintRequest;
import com.example.tabi.quest.actions.locationpuzzleaction.entity.LocationPuzzleAction;
import com.example.tabi.quest.actions.locationpuzzleaction.repository.LocationPuzzleActionRepository;
import com.example.tabi.quest.actions.locationpuzzleaction.vo.LocationPuzzleActionDto;
import com.example.tabi.quest.actions.locationpuzzleaction.vo.LocationPuzzleActionRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationPuzzleActionServiceJpaImpl implements LocationPuzzleActionService {
    private final HintService hintService;
    private final LocationPuzzleActionRepository locationPuzzleActionRepository;

    @Override
    @Transactional
    public LocationPuzzleAction createLocationPuzzleAction(LocationPuzzleActionRequest locationPuzzleActionRequest, HintRequest hintRequest) {
        LocationPuzzleAction locationPuzzleAction = new LocationPuzzleAction();

        locationPuzzleAction.setLocationName(locationPuzzleActionRequest.getLocationName());
        locationPuzzleAction.setActualLocation(locationPuzzleActionRequest.getActualLocation());
        locationPuzzleAction.setLatitude(locationPuzzleActionRequest.getLatitude());
        locationPuzzleAction.setLongitude(locationPuzzleActionRequest.getLongitude());
        locationPuzzleAction.setAltitude(locationPuzzleActionRequest.getAltitude());

        locationPuzzleAction.setHint(hintService.createHint(hintRequest));

        locationPuzzleAction.setCharacterImageUrl(locationPuzzleActionRequest.getCharacterImageUrl());
        locationPuzzleAction.setQuestStep(locationPuzzleActionRequest.getQuestStep());

        locationPuzzleActionRepository.save(locationPuzzleAction);

        return locationPuzzleAction;
    }

    @Override
    public LocationPuzzleAction retrieveLocationPuzzleAction(Long locationPuzzleActionId) {
        Optional<LocationPuzzleAction> locationPuzzleActionOptional = locationPuzzleActionRepository.findById(locationPuzzleActionId);

        return locationPuzzleActionOptional.orElse(null);
    }

    @Override
    @Transactional
    public LocationPuzzleAction updateLocationPuzzleAction(Long locationPuzzleActionId, LocationPuzzleActionRequest locationPuzzleActionRequest, HintRequest hintRequest) {
        Optional<LocationPuzzleAction> locationPuzzleActionOptional = locationPuzzleActionRepository.findById(locationPuzzleActionId);
        if (locationPuzzleActionOptional.isEmpty()) return null;

        LocationPuzzleAction locationPuzzleAction = locationPuzzleActionOptional.get();

        if (locationPuzzleActionRequest.getLocationName() != null)
            locationPuzzleAction.setLocationName(locationPuzzleActionRequest.getLocationName());

        if (locationPuzzleActionRequest.getActualLocation() != null)
            locationPuzzleAction.setActualLocation(locationPuzzleActionRequest.getActualLocation());

        if (locationPuzzleActionRequest.getLatitude() != null)
            locationPuzzleAction.setLatitude(locationPuzzleActionRequest.getLatitude());

        if (locationPuzzleActionRequest.getLongitude() != null)
            locationPuzzleAction.setLongitude(locationPuzzleActionRequest.getLongitude());

        if (locationPuzzleActionRequest.getAltitude() != null)
            locationPuzzleAction.setAltitude(locationPuzzleActionRequest.getAltitude());

        if (locationPuzzleActionRequest.getCharacterImageUrl() != null)
            locationPuzzleAction.setCharacterImageUrl(locationPuzzleActionRequest.getCharacterImageUrl());

        if (locationPuzzleActionRequest.getQuestStep() != null)
            locationPuzzleAction.setQuestStep(locationPuzzleActionRequest.getQuestStep());

        if (hintRequest != null)
            locationPuzzleAction.setHint(hintService.createHint(hintRequest));

        locationPuzzleActionRepository.save(locationPuzzleAction);

        return locationPuzzleAction;
    }

    @Override
    @Transactional
    public void deleteLocationPuzzleAction(Long locationPuzzleActionId) {
        locationPuzzleActionRepository.deleteById(locationPuzzleActionId);
    }
}

