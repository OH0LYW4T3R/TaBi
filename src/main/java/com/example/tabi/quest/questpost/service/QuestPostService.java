package com.example.tabi.quest.questpost.service;

import com.example.tabi.quest.questpost.vo.QuestPostDto;
import com.example.tabi.quest.questpost.vo.QuestPostRequest;
import org.springframework.security.core.Authentication;

public interface QuestPostService {
    QuestPostDto createQuestPost(Authentication authentication, QuestPostRequest questPostRequest);
}
