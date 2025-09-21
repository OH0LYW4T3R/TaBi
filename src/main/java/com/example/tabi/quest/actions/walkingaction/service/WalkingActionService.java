package com.example.tabi.quest.actions.walkingaction.service;

import com.example.tabi.quest.actions.walkingaction.entity.WalkingAction;
import com.example.tabi.quest.actions.walkingaction.vo.WalkingActionDto;
import com.example.tabi.quest.actions.walkingaction.vo.WalkingActionRequest;

public interface WalkingActionService {
    WalkingAction createWalkingAction(WalkingActionRequest walkingActionRequest);
    WalkingAction retrieveWalkingAction(Long walkingActionId);
    WalkingAction updateWalkingAction(Long walkingActionId, WalkingActionRequest walkingActionRequest);
    void deleteWalkingAction(Long walkingActionId);
}
