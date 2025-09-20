package com.example.tabi.quest.actions.stayingaction.service;

import com.example.tabi.quest.actions.stayingaction.entity.StayingAction;
import com.example.tabi.quest.actions.stayingaction.repository.StayingActionRepository;
import com.example.tabi.quest.actions.stayingaction.vo.StayingActionDto;
import com.example.tabi.quest.actions.stayingaction.vo.StayingActionRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StayingActionServiceJpaImpl implements StayingActionService {
    private final StayingActionRepository stayingActionRepository;

    @Override
    @Transactional
    public StayingActionDto createStayingAction(StayingActionRequest stayingActionRequest) {
        StayingAction stayingAction = new StayingAction();

        stayingAction.setDay(stayingActionRequest.getDay());
        stayingAction.setHour(stayingActionRequest.getHour());
        stayingAction.setMinute(stayingActionRequest.getMinute());
        stayingAction.setCharacterImageUrl(stayingActionRequest.getCharacterImageUrl());
        stayingAction.setQuestStep(stayingActionRequest.getQuestStep());

        stayingActionRepository.save(stayingAction);

        return stayingAction.actionToActionDto();
    }

    @Override
    public StayingActionDto retrieveStayingAction(Long stayingActionId) {
        Optional<StayingAction> stayingActionOptional = stayingActionRepository.findById(stayingActionId);
        if (stayingActionOptional.isEmpty()) return null;

        StayingAction stayingAction = stayingActionOptional.get();
        return stayingAction.actionToActionDto();
    }

    @Override
    @Transactional
    public StayingActionDto updateStayingAction(Long stayingActionId, StayingActionRequest stayingActionRequest) {
        Optional<StayingAction> stayingActionOptional = stayingActionRepository.findById(stayingActionId);
        if (stayingActionOptional.isEmpty()) return null;

        StayingAction stayingAction = stayingActionOptional.get();

        if (stayingActionRequest.getDay() != null)
            stayingAction.setDay(stayingActionRequest.getDay());

        if (stayingActionRequest.getHour() != null)
            stayingAction.setHour(stayingActionRequest.getHour());

        if (stayingActionRequest.getMinute() != null)
            stayingAction.setMinute(stayingActionRequest.getMinute());

        if (stayingActionRequest.getCharacterImageUrl() != null)
            stayingAction.setCharacterImageUrl(stayingActionRequest.getCharacterImageUrl());

        if (stayingActionRequest.getQuestStep() != null)
            stayingAction.setQuestStep(stayingActionRequest.getQuestStep());

        stayingActionRepository.save(stayingAction);

        return stayingAction.actionToActionDto();
    }

    @Override
    @Transactional
    public void deleteStayingAction(Long stayingActionId) {
        stayingActionRepository.deleteById(stayingActionId);
    }
}
