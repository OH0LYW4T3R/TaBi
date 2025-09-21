package com.example.tabi.quest.questindicating.service;

import com.example.tabi.quest.questindicating.entity.QuestIndicating;
import com.example.tabi.quest.questindicating.vo.QuestIndicatingDto;

public interface QuestIndicatingService {
    QuestIndicatingDto createQuestIndicating(Long questRunningLocationId);
    QuestIndicatingDto retrieveQuestIndicating(Long questIndicatingId);
    QuestIndicating updateQuestIndicating(QuestIndicating questIndicating);
}
