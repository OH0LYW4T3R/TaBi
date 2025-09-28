package com.example.tabi.quest.queststartlocation.service;

import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.quest.queststartlocation.entity.QuestStartLocation;

public interface QuestStartLocationService {
    QuestStartLocation createQuestStartLocation(QuestPost questPost);
}
