package com.example.tabi.quest.questpostcomment.vo;

import com.example.tabi.quest.questpostcomment.entity.QuestPostComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestPostCommentDto {
    private Long questPostCommentId;
    private Long questPostId;

    private Long appUserId;
    private String userName;
    private String profileImageUrl;

    private String comment;
    private Integer likeCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long parentId;
    private Integer childrenCount;

    private String errorMessage;

    public static QuestPostCommentDto questPostCommentToQuestPostCommentDto(QuestPostComment questPostComment, String errorMessage) {
        return new QuestPostCommentDto(
              questPostComment.getQuestPostCommentId(),
              questPostComment.getQuestPost().getQuestPostId(),
              questPostComment.getAppUser().getAppUserId(),
              questPostComment.getAppUser().getMyProfile().getNickName(),
              questPostComment.getAppUser().getMyProfile().getProfileImageUrl(),
              questPostComment.getComment(),
              questPostComment.getLikeCount(),
              questPostComment.getCreatedAt(),
              questPostComment.getUpdatedAt(),
              (questPostComment.getParent() == null ? null : questPostComment.getParent().getQuestPostCommentId()),
              questPostComment.getChildren().size(),
              errorMessage
        );
    }
}
