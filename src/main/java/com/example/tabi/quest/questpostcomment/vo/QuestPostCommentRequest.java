package com.example.tabi.quest.questpostcomment.vo;

import lombok.Data;

@Data
public class QuestPostCommentRequest {
    private Long questPostId;

    private String comment;

    private Long parentQuestPostCommentId;
}
