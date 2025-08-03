package com.example.tabi.treasurehunt.mytreasurehuntplay.service;

import com.example.tabi.treasurehunt.mytreasurehuntplay.PlayStatus;
import com.example.tabi.treasurehunt.mytreasurehuntplay.vo.MyTreasureHuntPlayDto;
import com.example.tabi.treasurehunt.mytreasurehuntplay.vo.PositionRequest;
import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import com.example.tabi.treasurehunt.treasurehuntpost.vo.TreasureHuntPostDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface MyTreasureHuntPlayService {
    // 보물 발견시 현재 위치 전송하여 근처면 성공시키는 함수
    MyTreasureHuntPlayDto clearedTreasureHuntPlay(Authentication authentication, PositionRequest positionRequest);
    // 목적지 반경내에 들어가서 이용가능한 형태로 전환
    MyTreasureHuntPlayDto changeToAvailableTreasureHuntPlay(Authentication authentication, PositionRequest positionRequest);
    // 뒤로 가기 클릭시 보류로 전환
    MyTreasureHuntPlayDto changeToSpecificStatusTreasureHuntPlay(Authentication authentication, PositionRequest positionRequest, PlayStatus playStatus);
    // 선택하기 클릭시 실행으로 전환
    MyTreasureHuntPlayDto changeToPlayingTreasureHuntPlay(Authentication authentication, PositionRequest positionRequest);
    // 클리어시 클리어로 전환
    MyTreasureHuntPlayDto changeToClearedTreasureHuntPlay(Authentication authentication);

    List<MyTreasureHuntPlayDto> getAvailableTreasureHuntPlays(Authentication authentication);
    List<MyTreasureHuntPlayDto> getSpecificStatusTreasureHuntPlays(Authentication authentication, PlayStatus playStatus);
}
