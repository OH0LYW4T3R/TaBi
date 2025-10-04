package com.example.tabi.quest.myquestplay.trace.questsavepoint.service;

import com.example.tabi.quest.myquestplay.entity.MyQuestPlay;
import com.example.tabi.quest.myquestplay.trace.questcurrentpoint.entity.QuestCurrentPoint;
import com.example.tabi.quest.myquestplay.trace.questcurrentpoint.repository.QuestCurrentPointRepository;
import com.example.tabi.quest.myquestplay.trace.questsavepoint.entity.QuestSavePoint;
import com.example.tabi.quest.myquestplay.trace.questsavepoint.repository.QuestSavePointRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestSavePointServiceJpaImpl implements QuestSavePointService {
    private final QuestSavePointRepository questSavePointRepository;
    private final QuestCurrentPointRepository questCurrentPointRepository;

    @Override
    @Transactional
    public QuestSavePoint createQuestSavePoint(MyQuestPlay myQuestPlay, QuestCurrentPoint questCurrentPoint) {
        QuestSavePoint questSavePoint = new QuestSavePoint();

        questSavePoint.setSaveQuestRunningLocationId(questCurrentPoint.getCurrentQuestRunningLocationId());
        questSavePoint.setSaveQuestRunningLocationNumber(questCurrentPoint.getCurrentQuestRunningLocationIndex());

        questSavePoint.setSaveActionId(questCurrentPoint.getCurrentActionId());
        questSavePoint.setSaveActionNumber(questCurrentPoint.getCurrentActionIndex());

        questSavePoint.setMyQuestPlay(myQuestPlay);

        questSavePointRepository.save(questSavePoint);

        return questSavePoint;
    }

    @Override
    public QuestSavePoint readQuestSavePoint(Long myQuestPlayId) {
        Optional<QuestSavePoint> questSavePoint = questSavePointRepository.findById(myQuestPlayId);

        return questSavePoint.orElse(null);
    }

    @Override
    @Transactional
    public QuestSavePoint updateQuestSavePoint(MyQuestPlay myQuestPlay, QuestCurrentPoint questCurrentPoint) {
        QuestSavePoint questSavePoint = myQuestPlay.getQuestSavePoint();

        questSavePoint.setSaveQuestRunningLocationId(questCurrentPoint.getCurrentQuestRunningLocationId());
        questSavePoint.setSaveQuestRunningLocationNumber(questCurrentPoint.getCurrentQuestRunningLocationIndex());

        questSavePoint.setSaveActionId(questCurrentPoint.getCurrentActionId());
        questSavePoint.setSaveActionNumber(questCurrentPoint.getCurrentActionIndex());

        questSavePointRepository.save(questSavePoint);

        return null;
    }

    @Override
    @Transactional
    public QuestSavePoint loadQuestSavePoint(QuestSavePoint questSavePoint, QuestCurrentPoint questCurrentPoint) {
        questCurrentPoint.setCurrentQuestRunningLocationId(questSavePoint.getSaveQuestRunningLocationId());
        questCurrentPoint.setCurrentQuestRunningLocationIndex(questSavePoint.getSaveQuestRunningLocationNumber());

        questCurrentPoint.setCurrentActionId(questSavePoint.getSaveActionId());
        questCurrentPoint.setCurrentActionIndex(questSavePoint.getSaveActionNumber());

        questSavePointRepository.save(questSavePoint);

        return questSavePoint;
    }

    @Override
    public void deleteQuestSavePoint(Long myQuestPlayId) {
        questSavePointRepository.deleteById(myQuestPlayId);
    }
}
