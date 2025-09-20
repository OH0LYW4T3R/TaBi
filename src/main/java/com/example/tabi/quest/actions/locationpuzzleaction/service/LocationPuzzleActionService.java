package com.example.tabi.quest.actions.locationpuzzleaction.service;

import com.example.tabi.quest.actions.hint.vo.HintRequest;
import com.example.tabi.quest.actions.locationpuzzleaction.vo.LocationPuzzleActionDto;
import com.example.tabi.quest.actions.locationpuzzleaction.vo.LocationPuzzleActionRequest;

public interface LocationPuzzleActionService {
    LocationPuzzleActionDto createLocationPuzzleAction(LocationPuzzleActionRequest locationPuzzleActionRequest, HintRequest hintRequest);
    LocationPuzzleActionDto retrieveLocationPuzzleAction(Long locationPuzzleActionId);
    LocationPuzzleActionDto updateLocationPuzzleAction(Long locationPuzzleActionId, LocationPuzzleActionRequest locationPuzzleActionRequest, HintRequest hintRequest);
    void deleteLocationPuzzleAction(Long locationPuzzleActionId);
}
