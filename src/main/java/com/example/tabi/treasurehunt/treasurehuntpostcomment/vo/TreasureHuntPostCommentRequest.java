package com.example.tabi.treasurehunt.treasurehuntpostcomment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TreasureHuntPostCommentRequest {
    private Long treasureHuntPostId;

    private String comment;

    private Long parentTreasureHuntPostCommentId;
}
