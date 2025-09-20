package com.example.tabi.quest.queststep.service;

import com.example.tabi.quest.queststep.vo.QuestStepDto;
import com.example.tabi.quest.queststep.vo.QuestStepRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestStepServiceJpaImpl implements QuestStepService {


    @Override
    public QuestStepDto createQuestStep(QuestStepRequest questStepRequest) {
        return null;
    }

    @Override
    public QuestStepDto updateQuestStep(Long questStepId, QuestStepRequest questStepRequest) {
        return null;
    }

    @Override
    public QuestStepDto retrieveQuestStep(Long questStepId) {
        return null;
    }

    @Override
    public void deleteQuestStep(Long questStepId) {

    }
}
