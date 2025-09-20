package com.example.tabi.quest.actions.stayingaction.service;

import com.example.tabi.quest.actions.stayingaction.vo.StayingActionDto;
import com.example.tabi.quest.actions.stayingaction.vo.StayingActionRequest;

public interface StayingActionService {
    StayingActionDto createStayingAction(StayingActionRequest stayingActionRequest);
    StayingActionDto retrieveStayingAction(Long stayingActionId);
    StayingActionDto updateStayingAction(Long stayingActionId, StayingActionRequest stayingActionRequest);
    void deleteStayingAction(Long stayingActionId);
}
