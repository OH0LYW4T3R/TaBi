package com.example.tabi.treasurehunt.treasurehuntpost.service;

import com.example.tabi.treasurehunt.treasurehuntpost.vo.TreasureHuntPostDto;
import com.example.tabi.treasurehunt.treasurehuntpost.vo.TreasureHuntPostRequest;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TreasureHuntPostService {
    TreasureHuntPostDto createTreasureHuntPost(Authentication authentication, TreasureHuntPostRequest treasureHuntPostRequest);
    // 현재 알고리즘 -> 단순히 최신의 것을 10개 보여주는것
    List<TreasureHuntPostDto> readTenTreasureHuntPosts(Authentication authentication, int pages);
}
