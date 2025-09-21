package com.example.tabi.quest.actions.locationpuzzleaction.service;

import com.example.tabi.quest.actions.hint.vo.HintRequest;
import com.example.tabi.quest.actions.locationpuzzleaction.entity.LocationPuzzleAction;
import com.example.tabi.quest.actions.locationpuzzleaction.vo.LocationPuzzleActionDto;
import com.example.tabi.quest.actions.locationpuzzleaction.vo.LocationPuzzleActionRequest;

public interface LocationPuzzleActionService {
    LocationPuzzleAction createLocationPuzzleAction(LocationPuzzleActionRequest locationPuzzleActionRequest, HintRequest hintRequest);
    LocationPuzzleAction retrieveLocationPuzzleAction(Long locationPuzzleActionId);
    LocationPuzzleAction updateLocationPuzzleAction(Long locationPuzzleActionId, LocationPuzzleActionRequest locationPuzzleActionRequest, HintRequest hintRequest);
    void deleteLocationPuzzleAction(Long locationPuzzleActionId);
}
