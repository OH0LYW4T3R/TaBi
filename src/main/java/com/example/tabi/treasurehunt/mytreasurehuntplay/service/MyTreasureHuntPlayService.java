package com.example.tabi.treasurehunt.mytreasurehuntplay.service;

import com.example.tabi.treasurehunt.mytreasurehuntplay.vo.MyTreasureHuntPlayDto;
import com.example.tabi.treasurehunt.mytreasurehuntplay.vo.PositionRequest;
import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import com.example.tabi.treasurehunt.treasurehuntpost.vo.TreasureHuntPostDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface MyTreasureHuntPlayService {
    // 실행중 상태인 보물찾기 리스트 넘기는 함수 (해당 정보로 프론트는 내부 저장소에 저장해서 사용자의 위치랑 저장한 위치랑 비교해야함 -> 그리고 목적지 반경 내면 changeToAvailableTreasureHuntPlay 호출)
    List<TreasureHuntPostDto> getRunningStatusTreasureHuntPosts(Authentication authentication);
    // 보물 발견시 현재 위치 전송하여 근처면 성공시키는 함수
    MyTreasureHuntPlayDto clearedTreasureHuntPlay(Authentication authentication, PositionRequest positionRequest);
    // 목적지 반경내에 들어가서 이용가능한 형태로 전환
    MyTreasureHuntPlayDto changeToAvailableTreasureHuntPlay(Authentication authentication, PositionRequest positionRequest);
    // 뒤로 가기 클릭시 보류로 전환
    MyTreasureHuntPlayDto changeToPendingTreasureHuntPlay(Authentication authentication);
    // 선택하기 클릭시 실행으로 전환
    MyTreasureHuntPlayDto changeToPlayingTreasureHuntPlay(Authentication authentication);
    // 클리어시 클리어로 전환
    MyTreasureHuntPlayDto changeToClearedTreasureHuntPlay(Authentication authentication);
}
