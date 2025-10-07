package com.example.tabi.quest.myquest.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.quest.questpost.vo.QuestPostDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface MyQuestService {
    void createMyQuest(AppUser appUser, QuestPost questPost);
    void playMyQuest(AppUser appUser, QuestPost questPost);
    List<QuestPostDto> getRunningStatusQuestPosts(Authentication authentication);
}
