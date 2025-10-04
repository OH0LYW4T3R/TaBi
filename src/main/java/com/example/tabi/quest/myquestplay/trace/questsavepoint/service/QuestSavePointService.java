package com.example.tabi.quest.myquestplay.trace.questsavepoint.service;

import com.example.tabi.quest.myquestplay.entity.MyQuestPlay;
import com.example.tabi.quest.myquestplay.trace.questcurrentpoint.entity.QuestCurrentPoint;
import com.example.tabi.quest.myquestplay.trace.questsavepoint.entity.QuestSavePoint;

public interface QuestSavePointService {
    QuestSavePoint createQuestSavePoint(MyQuestPlay myQuestPlay, QuestCurrentPoint questCurrentPoint);
    QuestSavePoint readQuestSavePoint(Long myQuestPlayId);
    QuestSavePoint updateQuestSavePoint(MyQuestPlay myQuestPlay, QuestCurrentPoint questCurrentPoint);
    QuestSavePoint loadQuestSavePoint(QuestSavePoint questSavePoint, QuestCurrentPoint questCurrentPoint);
    void deleteQuestSavePoint(Long myQuestPlayId);
}
