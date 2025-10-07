package com.example.tabi.quest.myquestplay.trace.questcurrentpoint.service;

import com.example.tabi.quest.actions.talkingaction.vo.TalkingActionDto;
import com.example.tabi.quest.myquestplay.entity.MyQuestPlay;
import com.example.tabi.quest.myquestplay.trace.questcurrentpoint.entity.QuestCurrentPoint;
import com.example.tabi.quest.myquestplay.trace.questcurrentpoint.vo.*;
import com.example.tabi.quest.questpost.entity.QuestPost;

public interface QuestCurrentPointService {
    QuestCurrentPoint createQuestCurrentPoint(MyQuestPlay myQuestPlay);
    QuestCurrentPointDto retrieveQuestCurrentPointDetail(Long myQuestPlayId); // 현재 보여줘야 할 액션 서빙
    QuestNextLocationDto retrieveQuestCurrentLocationInfo(Long myQuestPlayId); // 현재 위치 서빙
    QuestNextLocationDto retrieveQuestNextLocationSetting(Long myQuestPlayId, QuestUserLocationRequest questUserLocationRequest); // 다음 위치 도착시 셋팅
    QuestNextLocationDto retrieveQuestNextLocationInfo(Long myQuestPlayId); // 다음 위치 서빙
    QuestAnswerCheckDto checkAnswer(Long myQuestPlayId, QuestCurrentPointAnswerRequest questCurrentPointAnswerRequest);
    QuestMostRecentTalkingActionDto retrieveMostRecentTalkingAction(Long myQuestPlayId);
    void deleteQuestCurrentPointById(Long questCurrentPointId);
}
