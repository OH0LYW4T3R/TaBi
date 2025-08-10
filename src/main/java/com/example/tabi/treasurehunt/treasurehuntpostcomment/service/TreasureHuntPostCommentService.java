package com.example.tabi.treasurehunt.treasurehuntpostcomment.service;

import com.example.tabi.treasurehunt.treasurehuntpost.vo.TreasureHuntPostRequest;
import com.example.tabi.treasurehunt.treasurehuntpostcomment.vo.TreasureHuntPostCommentDto;
import com.example.tabi.treasurehunt.treasurehuntpostcomment.vo.TreasureHuntPostCommentRequest;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TreasureHuntPostCommentService {
    TreasureHuntPostCommentDto createTreasureHuntPostComment(Authentication authentication, TreasureHuntPostCommentRequest treasureHuntPostCommentRequest);
    String deleteTreasureHuntPostComment(Authentication authentication, Long commentId);
    List<TreasureHuntPostCommentDto> getTreasureHuntPostComments(Authentication authentication, TreasureHuntPostCommentRequest treasureHuntPostCommentRequest, int page);
    List<TreasureHuntPostCommentDto> getTreasureHuntPostChildrenComments(Authentication authentication, TreasureHuntPostCommentRequest treasureHuntPostCommentRequest, int page);

}
