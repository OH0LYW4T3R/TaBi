package com.example.tabi.quest.questpostcomment.service;

import com.example.tabi.quest.quest.entity.Quest;
import com.example.tabi.quest.questpostcomment.vo.QuestPostCommentDto;
import com.example.tabi.quest.questpostcomment.vo.QuestPostCommentRequest;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface QuestPostCommentService {
    QuestPostCommentDto createQuestPostComment(Authentication authentication, QuestPostCommentRequest questPostCommentRequest);
    String deleteQuestPostComment(Authentication authentication, Long questPostCommentId);
    List<QuestPostCommentDto> getQuestPostComments(Authentication authentication, QuestPostCommentRequest questPostCommentRequest, int page);
    List<QuestPostCommentDto> getQuestPostChildrenComments(Authentication authentication, QuestPostCommentRequest questPostCommentRequest, int page);
}
