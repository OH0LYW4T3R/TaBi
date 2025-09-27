package com.example.tabi.quest.questindicating.service;

import com.example.tabi.quest.questindicating.entity.QuestIndicating;
import com.example.tabi.quest.questindicating.vo.QuestIndicatingDto;
import com.example.tabi.quest.questrunninglocation.entity.QuestRunningLocation;

import java.util.List;

public interface QuestIndicatingService {
    QuestIndicating createQuestIndicating(QuestRunningLocation questRunningLocation);
    List<QuestIndicatingDto> retrieveQuestIndicatings(Long questRunningLocationId);
    QuestIndicatingDto retrieveQuestIndicating(Long questIndicatingId);
    QuestIndicating updateQuestIndicating(QuestIndicating questIndicating);
}
