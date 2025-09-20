package com.example.tabi.quest.queststep.service;

import com.example.tabi.quest.queststep.vo.QuestStepDto;
import com.example.tabi.quest.queststep.vo.QuestStepRequest;

public interface QuestStepService {
    QuestStepDto createQuestStep(QuestStepRequest questStepRequest);
    QuestStepDto updateQuestStep(Long questStepId, QuestStepRequest questStepRequest);
    QuestStepDto retrieveQuestStep(Long questStepId);
    void deleteQuestStep(Long questStepId);
}
