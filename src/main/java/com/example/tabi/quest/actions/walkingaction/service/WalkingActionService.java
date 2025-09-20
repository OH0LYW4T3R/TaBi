package com.example.tabi.quest.actions.walkingaction.service;

import com.example.tabi.quest.actions.walkingaction.vo.WalkingActionDto;
import com.example.tabi.quest.actions.walkingaction.vo.WalkingActionRequest;

public interface WalkingActionService {
    WalkingActionDto createWalkingAction(WalkingActionRequest walkingActionRequest);
    WalkingActionDto retrieveWalkingAction(Long walkingActionId);
    WalkingActionDto updateWalkingAction(Long walkingActionId, WalkingActionRequest walkingActionRequest);
    void deleteWalkingAction(Long walkingActionId);
}
