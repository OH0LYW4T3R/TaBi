package com.example.tabi.treasurehunt.mytreasurehunt.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.treasurehunt.mytreasurehunt.vo.MyTreasureHuntDto;
import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import com.example.tabi.treasurehunt.treasurehuntpost.vo.TreasureHuntPostDto;
import com.example.tabi.util.PlayStatus;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface MyTreasureHuntService {
    void createMyTreasureHunt(AppUser appUser, TreasureHuntPost treasureHuntPost);
    void playMyTreasureHunt(AppUser appUser, TreasureHuntPost treasureHuntPost);
    TreasureHuntPostDto saveMyTreasureHunt(Authentication authentication, Long treasureHuntPostId);

    List<TreasureHuntPostDto> getCreatedStatusTreasureHuntPosts(Authentication authentication);
    List<TreasureHuntPostDto> getSavedStatusTreasureHuntPosts(Authentication authentication);
    List<TreasureHuntPostDto> getTerminatedStatusTreasureHuntPosts(Authentication authentication);

    // 실행중 상태인 보물찾기 리스트 넘기는 함수 (해당 정보로 프론트는 내부 저장소에 저장해서 사용자의 위치랑 저장한 위치랑 비교해야함 -> 그리고 목적지 반경 내면 changeToAvailableTreasureHuntPlay 호출)
    List<TreasureHuntPostDto> getRunningStatusTreasureHuntPosts(Authentication authentication);

    List<TreasureHuntPostDto> getCreatedStatusTreasureHuntPostsForCounterparty(Long myProfileId);
    List<TreasureHuntPostDto> getSavedStatusTreasureHuntPostsForCounterparty(Long myProfileId);
    List<TreasureHuntPostDto> getTerminatedStatusTreasureHuntPostsForCounterparty(Long myProfileId);
    List<TreasureHuntPostDto> getRunningStatusTreasureHuntPostsForCounterparty(Long myProfileId);
}
