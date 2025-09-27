package com.example.tabi.quest.quest.service;

import com.example.tabi.quest.quest.entity.Quest;
import com.example.tabi.quest.questpost.entity.QuestPost;

public interface QuestService {
    Quest createQuest(QuestPost questPost);
}
