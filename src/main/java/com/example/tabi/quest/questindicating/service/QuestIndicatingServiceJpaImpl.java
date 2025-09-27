package com.example.tabi.quest.questindicating.service;

import com.example.tabi.quest.actions.action.entity.Action;
import com.example.tabi.quest.actions.inputpuzzleaction.entity.InputPuzzleAction;
import com.example.tabi.quest.actions.locationpuzzleaction.entity.LocationPuzzleAction;
import com.example.tabi.quest.actions.photopuzzleaction.entity.PhotoPuzzleAction;
import com.example.tabi.quest.actions.stayingaction.entity.StayingAction;
import com.example.tabi.quest.actions.talkingaction.entity.TalkingAction;
import com.example.tabi.quest.actions.walkingaction.entity.WalkingAction;
import com.example.tabi.quest.quest.entity.Quest;
import com.example.tabi.quest.questindicating.entity.QuestIndicating;
import com.example.tabi.quest.questindicating.repository.QuestIndicatingRepository;
import com.example.tabi.quest.questindicating.vo.QuestIndicatingDto;
import com.example.tabi.quest.questrunninglocation.entity.QuestRunningLocation;
import com.example.tabi.quest.questrunninglocation.repository.QuestRunningLocationRepository;
import com.example.tabi.quest.queststep.entity.QuestStep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestIndicatingServiceJpaImpl implements QuestIndicatingService {
    private final QuestIndicatingRepository questIndicatingRepository;
    private final QuestRunningLocationRepository questRunningLocationRepository;

    @Override
    public QuestIndicating createQuestIndicating(QuestRunningLocation questRunningLocation) {
        QuestIndicating questIndicating = new QuestIndicating();
        questIndicating.setActionCount(0);
        questIndicating.setTalkingAction(false);
        questIndicating.setStayingAction(false);
        questIndicating.setPuzzleAction(false);
        questIndicating.setWalkingAction(false);

        questIndicating.setQuestRunningLocation(questRunningLocation);

        questIndicatingRepository.save(questIndicating);

        return questIndicating;
    }

    @Override
    public List<QuestIndicatingDto> retrieveQuestIndicatings(Long questRunningLocationId) {
        Optional<QuestRunningLocation> questRunningLocationOptional = questRunningLocationRepository.findById(questRunningLocationId);
        if (questRunningLocationOptional.isEmpty()) return null;

        Quest quest = questRunningLocationOptional.get().getQuest();
        List<QuestRunningLocation> questRunningLocations = quest.getQuestRunningLocations();

        List<QuestIndicatingDto> questIndicatingDtos = new ArrayList<>();

        for (QuestRunningLocation questRunningLocation : questRunningLocations) {
            QuestIndicating questIndicating = questRunningLocation.getQuestIndicating();
            questIndicatingDtos.add(QuestIndicatingDto.questIndicatingToQuestIndicatingDto(questIndicating));
        }

        return questIndicatingDtos;
    }

    @Override
    public QuestIndicatingDto retrieveQuestIndicating(Long questIndicatingId) {
        Optional<QuestIndicating> questIndicatingOptional = questIndicatingRepository.findById(questIndicatingId);
        if (questIndicatingOptional.isEmpty()) return null;

        QuestIndicating updatedQuestIndicating = updateQuestIndicating(questIndicatingOptional.get());
        return QuestIndicatingDto.questIndicatingToQuestIndicatingDto(updatedQuestIndicating);
    }

    @Override
    public QuestIndicating updateQuestIndicating(QuestIndicating questIndicating) {
        List<QuestStep> questSteps = questIndicating.getQuestSteps();

        questIndicating.setActionCount(questSteps.size());

        for (QuestStep questStep : questSteps) {
            // action = (Action) org.hibernate.Hibernate.unproxy(action); -> 프록시 문제 발생시 해당 코드로 변경
            Action action = questStep.getAction();

            if (action instanceof StayingAction stayingAction)
                questIndicating.setStayingAction(true);
            else if (action instanceof TalkingAction talkingAction)
                questIndicating.setTalkingAction(true);
            else if (action instanceof WalkingAction walkingAction)
                questIndicating.setWalkingAction(true);
            else if (action instanceof InputPuzzleAction || action instanceof LocationPuzzleAction || action instanceof PhotoPuzzleAction)
                questIndicating.setPuzzleAction(true);
            else
                throw new IllegalArgumentException("지원하지 않는 Action subtype: " + action.getClass());
        }

        questIndicatingRepository.save(questIndicating);

        return questIndicating;
    }
}
