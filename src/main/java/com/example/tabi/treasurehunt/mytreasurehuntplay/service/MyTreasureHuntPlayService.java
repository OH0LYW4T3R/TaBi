package com.example.tabi.treasurehunt.mytreasurehuntplay.service;

import com.example.tabi.treasurehunt.mytreasurehuntplay.PlayStatus;
import com.example.tabi.treasurehunt.mytreasurehuntplay.vo.MyTreasureHuntPlayDto;
import com.example.tabi.treasurehunt.mytreasurehuntplay.vo.PositionRequest;
import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import com.example.tabi.treasurehunt.treasurehuntpost.vo.TreasureHuntPostDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface MyTreasureHuntPlayService {
    MyTreasureHuntPlayDto changeToSpecificStatusTreasureHuntPlay(Authentication authentication, PositionRequest positionRequest, PlayStatus playStatus);
    List<MyTreasureHuntPlayDto> getSpecificStatusTreasureHuntPlays(Authentication authentication, PlayStatus playStatus);
}
