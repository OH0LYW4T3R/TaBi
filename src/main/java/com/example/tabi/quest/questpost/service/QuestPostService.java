package com.example.tabi.quest.questpost.service;

import com.example.tabi.quest.questpost.vo.FinalSettingQuestPostRequest;
import com.example.tabi.quest.questpost.vo.QuestPostDto;
import com.example.tabi.quest.questpost.vo.InitialSettingQuestPostRequest;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface QuestPostService {
    QuestPostDto initialSettingQuestPost(Authentication authentication);
    QuestPostDto finalSettingQuestPost(FinalSettingQuestPostRequest finalSettingQuestPostRequest);
    List<QuestPostDto> getQuestPosts(Authentication authentication, int pages);
}
