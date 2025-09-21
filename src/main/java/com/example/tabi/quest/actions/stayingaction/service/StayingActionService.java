package com.example.tabi.quest.actions.stayingaction.service;

import com.example.tabi.quest.actions.stayingaction.entity.StayingAction;
import com.example.tabi.quest.actions.stayingaction.vo.StayingActionDto;
import com.example.tabi.quest.actions.stayingaction.vo.StayingActionRequest;

public interface StayingActionService {
    StayingAction createStayingAction(StayingActionRequest stayingActionRequest);
    StayingAction retrieveStayingAction(Long stayingActionId);
    StayingAction updateStayingAction(Long stayingActionId, StayingActionRequest stayingActionRequest);
    void deleteStayingAction(Long stayingActionId);
}
