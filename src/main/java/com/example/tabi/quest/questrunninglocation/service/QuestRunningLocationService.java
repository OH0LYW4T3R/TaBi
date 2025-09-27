package com.example.tabi.quest.questrunninglocation.service;

import com.example.tabi.quest.questrunninglocation.vo.QuestRunningLocationDto;
import com.example.tabi.quest.questrunninglocation.vo.QuestRunningLocationRequest;

import java.util.List;

public interface QuestRunningLocationService {
    List<QuestRunningLocationDto> saveQuestRunningLocation(Long questId, List<QuestRunningLocationRequest> questRunningLocationRequests);
    List<QuestRunningLocationDto> getQuestRunningLocations(Long questId);
    void deleteQuestRunningLocation(Long questRunningLocationId);
}
