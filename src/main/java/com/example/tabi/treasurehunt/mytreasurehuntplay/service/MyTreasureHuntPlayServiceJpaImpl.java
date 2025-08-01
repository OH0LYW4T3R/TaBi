package com.example.tabi.treasurehunt.mytreasurehuntplay.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.repository.AppUserRepository;
import com.example.tabi.appuser.service.AppUserServiceJpaImpl;
import com.example.tabi.member.repository.MemberRepository;
import com.example.tabi.treasurehunt.mytreasurehunt.PostStatus;
import com.example.tabi.treasurehunt.mytreasurehunt.repository.MyTreasureHuntRepository;
import com.example.tabi.treasurehunt.mytreasurehunt.service.MyTreasureHuntServiceJpaImpl;
import com.example.tabi.treasurehunt.mytreasurehuntplay.entity.MyTreasureHuntPlay;
import com.example.tabi.treasurehunt.mytreasurehuntplay.repository.MyTreasureHuntPlayRepository;
import com.example.tabi.treasurehunt.mytreasurehuntplay.vo.MyTreasureHuntPlayDto;
import com.example.tabi.treasurehunt.mytreasurehuntplay.vo.PositionRequest;
import com.example.tabi.treasurehunt.treasurehuntpost.service.TreasureHuntPostServiceJpaImpl;
import com.example.tabi.treasurehunt.treasurehuntpost.vo.TreasureHuntPostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyTreasureHuntPlayServiceJpaImpl implements MyTreasureHuntPlayService {
    private final AppUserRepository appUserRepository;
    private final MemberRepository memberRepository;
    private final MyTreasureHuntPlayRepository myTreasureHuntPlayRepository;
    private final MyTreasureHuntRepository myTreasureHuntRepository;

    @Override
    public List<TreasureHuntPostDto> getRunningStatusTreasureHuntPosts(Authentication authentication) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();


        return myTreasureHuntRepository.findByAppUserAndStatus(appUser, PostStatus.RUNNING).stream().map(
                treasureHunt -> TreasureHuntPostServiceJpaImpl.treasureHuntPostToTreasureHuntPostDto(
                        treasureHunt.getTreasureHuntPost(),
                        treasureHunt.getTreasureHuntPost().getPostCounter(),
                        treasureHunt.getTreasureHuntPost().getReward(),
                        treasureHunt.getTreasureHuntPost().getTreasureHuntStartLocation(),
                        treasureHunt.getTreasureHuntPost().getTreasureHuntPostImages()
                )).toList();
    }

    @Override
    public MyTreasureHuntPlayDto clearedTreasureHuntPlay(Authentication authentication, PositionRequest positionRequest) {
        // myTreasureHuntPlay를 cleared 시키고 TreasureHuntPost에 연결된 모든 myTreasureHunt를 terminated로 변경후 TreasureHuntPost의 termination부분도 true
        return null;
    }

    @Override
    public MyTreasureHuntPlayDto changeToAvailableTreasureHuntPlay(Authentication authentication, PositionRequest positionRequest) {
        // 다른 누군가가 완료하지 않아서 종료되지 않았으며 목적지 반경에 들었는가를 확인
        return null;
    }

    @Override
    public MyTreasureHuntPlayDto changeToPendingTreasureHuntPlay(Authentication authentication) {
        return null;
    }

    @Override
    public MyTreasureHuntPlayDto changeToPlayingTreasureHuntPlay(Authentication authentication) {
        return null;
    }

    @Override
    public MyTreasureHuntPlayDto changeToClearedTreasureHuntPlay(Authentication authentication) {
        return null;
    }

    public static MyTreasureHuntPlayDto myTreasureHuntPlayToMyTreasureHuntPlayDto(MyTreasureHuntPlay play) {
        return new MyTreasureHuntPlayDto(
            play.getMyTreasureHuntPlayId(),
            play.getAppUser().getAppUserId(),
            play.getTreasureHuntPost().getTreasureHuntPostId(),
            play.getPlayStatus(),
            play.getCreatedAt(),
            play.getUpdatedAt()
        );
    }
}
