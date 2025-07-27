package com.example.tabi.treasurehunt.treasurehuntpost.service;

import com.example.tabi.treasurehunt.treasurehuntpost.vo.TreasureHuntPostDto;
import com.example.tabi.treasurehunt.treasurehuntpost.vo.TreasureHuntPostRequest;
import org.springframework.security.core.Authentication;

public interface TreasureHuntPostService {
    TreasureHuntPostDto createTreasureHuntPost(Authentication authentication, TreasureHuntPostRequest treasureHuntPostRequest);
}
