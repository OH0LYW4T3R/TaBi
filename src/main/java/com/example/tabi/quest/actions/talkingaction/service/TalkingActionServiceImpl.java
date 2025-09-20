package com.example.tabi.quest.actions.talkingaction.service;

import com.example.tabi.quest.actions.talkingaction.entity.TalkingAction;
import com.example.tabi.quest.actions.talkingaction.repository.TalkingActionRepository;
import com.example.tabi.quest.actions.talkingaction.vo.TalkingActionDto;
import com.example.tabi.quest.actions.talkingaction.vo.TalkingActionRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TalkingActionServiceImpl implements TalkingActionService {
    private final TalkingActionRepository talkingActionRepository;

    @Override
    @Transactional
    public TalkingActionDto createTalkingAction(TalkingActionRequest talkingActionRequest) {
        TalkingAction talkingAction = new TalkingAction();

        talkingAction.setStory(talkingActionRequest.getStory());
        talkingAction.setCharacterImageUrl(talkingActionRequest.getCharacterImageUrl());
        talkingAction.setQuestStep(talkingActionRequest.getQuestStep());

        talkingActionRepository.save(talkingAction);

        return talkingAction.actionToActionDto();
    }

    @Override
    public TalkingActionDto retrieveTalkingAction(Long talkingActionId) {
        Optional<TalkingAction> talkingActionOptional = talkingActionRepository.findById(talkingActionId);
        if (talkingActionOptional.isEmpty()) return null;

        TalkingAction talkingAction = talkingActionOptional.get();
        return talkingAction.actionToActionDto();
    }

    @Override
    @Transactional
    public TalkingActionDto updateTalkingAction(Long talkingActionId, TalkingActionRequest talkingActionRequest) {
        Optional<TalkingAction> talkingActionOptional = talkingActionRepository.findById(talkingActionId);
        if (talkingActionOptional.isEmpty()) return null;

        TalkingAction talkingAction = talkingActionOptional.get();

        if (talkingActionRequest.getStory() != null)
            talkingAction.setStory(talkingActionRequest.getStory());

        if (talkingActionRequest.getCharacterImageUrl() != null)
            talkingAction.setCharacterImageUrl(talkingActionRequest.getCharacterImageUrl());

        if (talkingActionRequest.getQuestStep() != null)
            talkingAction.setQuestStep(talkingActionRequest.getQuestStep());

        talkingActionRepository.save(talkingAction);

        return talkingAction.actionToActionDto();
    }

    @Override
    @Transactional
    public void deleteTalkingAction(Long talkingActionId) {
        talkingActionRepository.deleteById(talkingActionId);
    }
}
