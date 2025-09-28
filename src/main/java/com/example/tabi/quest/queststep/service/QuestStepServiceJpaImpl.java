package com.example.tabi.quest.queststep.service;

import com.example.tabi.quest.actions.action.entity.Action;
import com.example.tabi.quest.actions.hint.entity.Hint;
import com.example.tabi.quest.actions.hint.vo.HintRequest;
import com.example.tabi.quest.actions.inputpuzzleaction.service.InputPuzzleActionService;
import com.example.tabi.quest.actions.inputpuzzleaction.vo.InputPuzzleActionRequest;
import com.example.tabi.quest.actions.inputpuzzleaction.vo.InputPuzzleActionRequestAndHintRequest;
import com.example.tabi.quest.actions.locationpuzzleaction.service.LocationPuzzleActionService;
import com.example.tabi.quest.actions.locationpuzzleaction.vo.LocationPuzzleActionRequest;
import com.example.tabi.quest.actions.locationpuzzleaction.vo.LocationPuzzleActionRequestAndHintRequest;
import com.example.tabi.quest.actions.photopuzzleaction.service.PhotoPuzzleActionService;
import com.example.tabi.quest.actions.photopuzzleaction.vo.PhotoPuzzleActionRequest;
import com.example.tabi.quest.actions.photopuzzleaction.vo.PhotoPuzzleActionRequestAndHintRequest;
import com.example.tabi.quest.actions.stayingaction.service.StayingActionService;
import com.example.tabi.quest.actions.stayingaction.vo.StayingActionRequest;
import com.example.tabi.quest.actions.talkingaction.service.TalkingActionService;
import com.example.tabi.quest.actions.talkingaction.vo.TalkingActionRequest;
import com.example.tabi.quest.actions.walkingaction.service.WalkingActionService;
import com.example.tabi.quest.actions.walkingaction.vo.WalkingActionRequest;
import com.example.tabi.quest.questindicating.entity.QuestIndicating;
import com.example.tabi.quest.questindicating.repository.QuestIndicatingRepository;
import com.example.tabi.quest.queststep.entity.QuestStep;
import com.example.tabi.quest.queststep.repository.QuestStepRepository;
import com.example.tabi.quest.queststep.vo.QuestStepDto;
import com.example.tabi.quest.queststep.vo.QuestStepRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestStepServiceJpaImpl implements QuestStepService {
    private final QuestIndicatingRepository questIndicatingRepository;
    private final QuestStepRepository questStepRepository;

    private final TalkingActionService talkingActionService;
    private final StayingActionService stayingActionService;
    private final WalkingActionService walkingActionService;
    private final InputPuzzleActionService inputPuzzleActionService;
    private final LocationPuzzleActionService locationPuzzleActionService;
    private final PhotoPuzzleActionService photoPuzzleActionService;

    @Override
    @Transactional
    public QuestStepDto createQuestStep(QuestStepRequest questStepRequest) {
        QuestStep questStep = new QuestStep();

        Optional<QuestIndicating> questIndicatingOptional = questIndicatingRepository.findById(questStepRequest.getQuestIndicatingId());
        if (questIndicatingOptional.isEmpty()) return null; // Not there Exist Quest Indicating ID

        questStep.setQuestIndicating(questIndicatingOptional.get());
        questStep.setSequence(questStepRequest.getSequence());

        if (questStepRequest.getActionType() != null) {
            Action action = null;

            switch (questStepRequest.getActionType()) {
                case TALKING -> {
                    TalkingActionRequest talkingActionRequest = TalkingActionRequest.prepareTalkingActionRequest(questStepRequest, questStep);
                    action = talkingActionService.createTalkingAction(talkingActionRequest);
                }
                case STAYING -> {
                    StayingActionRequest stayingActionRequest = StayingActionRequest.prepareStayingActionRequest(questStepRequest, questStep);
                    action = stayingActionService.createStayingAction(stayingActionRequest);
                }
                case WALKING -> {
                    WalkingActionRequest walkingActionRequest = WalkingActionRequest.prepareWalkingActionRequest(questStepRequest, questStep);
                    action = walkingActionService.createWalkingAction(walkingActionRequest);
                }
                case INPUT_PUZZLE -> {
                    InputPuzzleActionRequestAndHintRequest inputPuzzleActionRequestAndHintRequest = InputPuzzleActionRequestAndHintRequest.prepareInputPuzzleActionRequestAndHintRequest(questStepRequest, questStep);
                    action = inputPuzzleActionService.createInputPuzzleAction(inputPuzzleActionRequestAndHintRequest.getInputPuzzleActionRequest(), inputPuzzleActionRequestAndHintRequest.getHintRequest());
                }
                case LOCATION_PUZZLE -> {
                    LocationPuzzleActionRequestAndHintRequest locationPuzzleActionRequestAndHintRequest = LocationPuzzleActionRequestAndHintRequest.prepareLocationPuzzleActionRequestAndHintRequest(questStepRequest, questStep);
                    action = locationPuzzleActionService.createLocationPuzzleAction(locationPuzzleActionRequestAndHintRequest.getLocationPuzzleActionRequest(), locationPuzzleActionRequestAndHintRequest.getHintRequest());
                }
                case PHOTO_PUZZLE -> {
                    PhotoPuzzleActionRequestAndHintRequest photoPuzzleActionRequestAndHintRequest = PhotoPuzzleActionRequestAndHintRequest.preparePhotoPuzzleActionRequestAndHintRequest(questStepRequest, questStep);
                    action = photoPuzzleActionService.createPhotoPuzzleAction(photoPuzzleActionRequestAndHintRequest.getPhotoPuzzleActionRequest(), photoPuzzleActionRequestAndHintRequest.getHintRequest());
                }
            }

            questStep.setAction(action);
        } else return null; // Not there Exist ActionType in Quest Step Request

        questStepRepository.save(questStep);

        return QuestStepDto.questStepToQuestStepDto(questStep);
    }

    @Override
    public QuestStepDto updateQuestStep(Long questStepId, QuestStepRequest questStepRequest) {
        Optional<QuestStep> questStepOptional = questStepRepository.findById(questStepId);
        if (questStepOptional.isEmpty()) return null;

        QuestStep questStep = questStepOptional.get();

        questStep.setSequence(questStepRequest.getSequence());

        if (questStepRequest.getActionType() != null) {
            // action = (Action) org.hibernate.Hibernate.unproxy(action); -> 프록시 문제 발생시 해당 코드로 변경
            Action action = questStep.getAction();
            if (action == null) return QuestStepDto.questStepToQuestStepDto(questStep);

            switch (questStepRequest.getActionType()) {
                case TALKING -> {
                    TalkingActionRequest talkingActionRequest = TalkingActionRequest.prepareTalkingActionRequest(questStepRequest, questStep);
                    action = talkingActionService.updateTalkingAction(action.getActionId(), talkingActionRequest);
                }
                case STAYING -> {
                    StayingActionRequest stayingActionRequest = StayingActionRequest.prepareStayingActionRequest(questStepRequest, questStep);
                    action = stayingActionService.updateStayingAction(action.getActionId(), stayingActionRequest);
                }
                case WALKING -> {
                    WalkingActionRequest walkingActionRequest = WalkingActionRequest.prepareWalkingActionRequest(questStepRequest, questStep);
                    action = walkingActionService.updateWalkingAction(action.getActionId(), walkingActionRequest);
                }
                case INPUT_PUZZLE -> {
                    InputPuzzleActionRequestAndHintRequest inputPuzzleActionRequestAndHintRequest = InputPuzzleActionRequestAndHintRequest.prepareInputPuzzleActionRequestAndHintRequest(questStepRequest, questStep);
                    action = inputPuzzleActionService.updateInputPuzzleAction(action.getActionId(), inputPuzzleActionRequestAndHintRequest.getInputPuzzleActionRequest(), inputPuzzleActionRequestAndHintRequest.getHintRequest());
                }
                case LOCATION_PUZZLE -> {
                    LocationPuzzleActionRequestAndHintRequest locationPuzzleActionRequestAndHintRequest = LocationPuzzleActionRequestAndHintRequest.prepareLocationPuzzleActionRequestAndHintRequest(questStepRequest, questStep);
                    action = locationPuzzleActionService.updateLocationPuzzleAction(action.getActionId(), locationPuzzleActionRequestAndHintRequest.getLocationPuzzleActionRequest(), locationPuzzleActionRequestAndHintRequest.getHintRequest());
                }
                case PHOTO_PUZZLE -> {
                    PhotoPuzzleActionRequestAndHintRequest photoPuzzleActionRequestAndHintRequest = PhotoPuzzleActionRequestAndHintRequest.preparePhotoPuzzleActionRequestAndHintRequest(questStepRequest, questStep);
                    action = photoPuzzleActionService.updatePhotoPuzzleAction(action.getActionId(), photoPuzzleActionRequestAndHintRequest.getPhotoPuzzleActionRequest(), photoPuzzleActionRequestAndHintRequest.getHintRequest());
                }
            }

            questStep.setAction(action);
            questStepRepository.save(questStep);
        }

        return QuestStepDto.questStepToQuestStepDto(questStep);
    }

    @Override
    public QuestStepDto retrieveQuestStep(Long questStepId) {
        Optional<QuestStep> questStepOptional = questStepRepository.findById(questStepId);
        return questStepOptional.map(QuestStepDto::questStepToQuestStepDto).orElse(null);
    }

    @Override
    public void deleteQuestStep(Long questStepId) {
        questStepRepository.deleteById(questStepId);
    }
}
