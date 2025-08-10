package com.example.tabi.treasurehunt.treasurehuntpostcomment.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreasureHuntPostCommentDto {
    private Long treasureHuntPostCommentId;
    private Long treasureHuntPostId;   // post FK

    private Long appUserId;            // 작성자
    private String userName;
    private String profileImageUrl;

    private String comment;
    private Integer likeCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long parentId;             // 부모 댓글(최상위면 null)
    private Integer childrenCount;     // 대댓글 개수(가벼운 응답용)

    private String errorMessage;
}