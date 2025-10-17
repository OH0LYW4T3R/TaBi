package com.example.tabi.quest.myquest.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.quest.quest.entity.Quest;
import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.quest.questpost.vo.QuestPostDto;
import com.example.tabi.treasurehunt.treasurehuntpost.vo.TreasureHuntPostDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface MyQuestService {
    void createMyQuest(AppUser appUser, QuestPost questPost);
    void playMyQuest(AppUser appUser, QuestPost questPost);
    QuestPostDto saveMyQuest(Authentication authentication, Long questPostId);

    List<QuestPostDto> getCreatedStatusQuestPosts(Authentication authentication);
    List<QuestPostDto> getSavedStatusQuestPosts(Authentication authentication);
    List<QuestPostDto> getTerminatedQuestPosts(Authentication authentication);
    List<QuestPostDto> getRunningStatusQuestPosts(Authentication authentication);

    List<QuestPostDto> getCreatedStatusQuestPostsForCounterparty(Long myProfileId);
    List<QuestPostDto> getSavedStatusQuestPostsForCounterparty(Long myProfileId);
    List<QuestPostDto> getTerminatedStatusQuestPostsForCounterparty(Long myProfileId);
    List<QuestPostDto> getRunningStatusQuestPostsForCounterparty(Long myProfileId);
}
