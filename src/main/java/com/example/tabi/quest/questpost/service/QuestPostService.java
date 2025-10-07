package com.example.tabi.quest.questpost.service;

import com.example.tabi.quest.questpost.vo.FinalSettingQuestPostRequest;
import com.example.tabi.quest.questpost.vo.FullQuestPostDto;
import com.example.tabi.quest.questpost.vo.QuestPostDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface QuestPostService {
    QuestPostDto initialSettingQuestPost(Authentication authentication);
    FullQuestPostDto finalSettingQuestPost(FinalSettingQuestPostRequest finalSettingQuestPostRequest);
    List<QuestPostDto> readTenQuestPosts(Authentication authentication, int pages);
    String playQuestPost(Authentication authentication, Long questPostId);
}
