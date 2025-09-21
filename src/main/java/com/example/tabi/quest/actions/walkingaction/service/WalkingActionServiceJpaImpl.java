package com.example.tabi.quest.actions.walkingaction.service;

import com.example.tabi.quest.actions.walkingaction.entity.WalkingAction;
import com.example.tabi.quest.actions.walkingaction.repository.WalkingActionRepository;
import com.example.tabi.quest.actions.walkingaction.vo.WalkingActionDto;
import com.example.tabi.quest.actions.walkingaction.vo.WalkingActionRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalkingActionServiceJpaImpl implements WalkingActionService {
    private final WalkingActionRepository walkingActionRepository;

    @Override
    @Transactional
    public WalkingAction createWalkingAction(WalkingActionRequest walkingActionRequest) {
        WalkingAction walkingAction = new WalkingAction();

        walkingAction.setWalkingCount(walkingActionRequest.getWalkingCount());
        walkingAction.setCharacterImageUrl(walkingActionRequest.getCharacterImageUrl());
        walkingAction.setQuestStep(walkingActionRequest.getQuestStep());

        walkingActionRepository.save(walkingAction);

        return walkingAction;
    }

    @Override
    public WalkingAction retrieveWalkingAction(Long walkingActionId) {
        Optional<WalkingAction> optionalWalkingAction = walkingActionRepository.findById(walkingActionId);

        return optionalWalkingAction.orElse(null);
    }

    @Override
    @Transactional
    public WalkingAction updateWalkingAction(Long walkingActionId, WalkingActionRequest walkingActionRequest) {
        Optional<WalkingAction> optionalWalkingAction = walkingActionRepository.findById(walkingActionId);

        if (optionalWalkingAction.isEmpty())
            return null;

        WalkingAction walkingAction = optionalWalkingAction.get();

        if (walkingActionRequest.getWalkingCount() != null)
            walkingAction.setWalkingCount(walkingActionRequest.getWalkingCount());

        if (walkingActionRequest.getCharacterImageUrl() != null)
            walkingAction.setCharacterImageUrl(walkingActionRequest.getCharacterImageUrl());

        if (walkingActionRequest.getQuestStep() != null)
            walkingAction.setQuestStep(walkingActionRequest.getQuestStep());

        walkingActionRepository.save(walkingAction);

        return walkingAction;
    }

    @Override
    @Transactional
    public void deleteWalkingAction(Long walkingActionId) {
        walkingActionRepository.deleteById(walkingActionId);
    }
}
