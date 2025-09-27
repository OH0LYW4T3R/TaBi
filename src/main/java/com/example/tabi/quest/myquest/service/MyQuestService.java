package com.example.tabi.quest.myquest.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.quest.questpost.entity.QuestPost;

public interface MyQuestService {
    public void createMyQuest(AppUser appUser, QuestPost questPost);
}
