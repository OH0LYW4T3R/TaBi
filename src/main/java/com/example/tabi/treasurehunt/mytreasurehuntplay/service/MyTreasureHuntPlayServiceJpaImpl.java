package com.example.tabi.treasurehunt.mytreasurehuntplay.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.repository.AppUserRepository;
import com.example.tabi.appuser.service.AppUserServiceJpaImpl;
import com.example.tabi.member.repository.MemberRepository;
import com.example.tabi.treasurehunt.mytreasurehunt.PostStatus;
import com.example.tabi.treasurehunt.mytreasurehunt.entity.MyTreasureHunt;
import com.example.tabi.treasurehunt.mytreasurehunt.repository.MyTreasureHuntRepository;
import com.example.tabi.treasurehunt.mytreasurehunt.service.MyTreasureHuntServiceJpaImpl;
import com.example.tabi.treasurehunt.mytreasurehuntplay.PlayStatus;
import com.example.tabi.treasurehunt.mytreasurehuntplay.entity.MyTreasureHuntPlay;
import com.example.tabi.treasurehunt.mytreasurehuntplay.repository.MyTreasureHuntPlayRepository;
import com.example.tabi.treasurehunt.mytreasurehuntplay.vo.MyTreasureHuntPlayDto;
import com.example.tabi.treasurehunt.mytreasurehuntplay.vo.PositionRequest;
import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import com.example.tabi.treasurehunt.treasurehuntpost.repository.TreasureHuntPostRepository;
import com.example.tabi.treasurehunt.treasurehuntpost.service.TreasureHuntPostServiceJpaImpl;
import com.example.tabi.treasurehunt.treasurehuntpost.vo.TreasureHuntPostDto;
import com.example.tabi.util.GeoUtil;
import jakarta.transaction.Transactional;
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
    private static final double BASE_RADIUS_KM = 1.0;
    private static final double SUCCESS_RADIUS_KM = 0.0015; // 1.5m
    private final AppUserRepository appUserRepository;
    private final MemberRepository memberRepository;
    private final MyTreasureHuntPlayRepository myTreasureHuntPlayRepository;
    private final MyTreasureHuntRepository myTreasureHuntRepository;
    private final TreasureHuntPostRepository treasureHuntPostRepository;

    @Override
    @Transactional
    public MyTreasureHuntPlayDto clearedTreasureHuntPlay(Authentication authentication, PositionRequest positionRequest) {
        // myTreasureHuntPlay를 cleared 시키고 TreasureHuntPost에 연결된 모든 myTreasureHunt를 terminated로 변경후 TreasureHuntPost의 termination부분도 true
        // 클리어시 treasureHuntPost termination을 true로 하고 참여자 전부 MyTreasureHunt의 status를 Terminated로 바꿈
        MyTreasureHuntPlayDto myTreasureHuntPlayDto = new MyTreasureHuntPlayDto();

        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty()) {
            myTreasureHuntPlayDto.setErrorMessage("AppUser Not Found");
            return myTreasureHuntPlayDto;
        }

        AppUser appUser = optionalAppUser.get();

        TreasureHuntPost treasureHuntPost = treasureHuntPostRepository.findById(positionRequest.getTreasureHuntPostId()).orElse(null);

        if (treasureHuntPost == null) {
            myTreasureHuntPlayDto.setErrorMessage("Already Deleted or AppUser Not Found"); // 삭제되거나 없는 포스트
            return myTreasureHuntPlayDto;
        }

        if (treasureHuntPost.isTermination()) {
            myTreasureHuntPlayDto.setErrorMessage("Already Terminated Treasure Hunt Posting");
            return myTreasureHuntPlayDto;
        }

        if (!GeoUtil.isWithinRadius(treasureHuntPost.getTreasureHuntStartLocation().getLatitude(), treasureHuntPost.getTreasureHuntStartLocation().getLongitude(), positionRequest.getLatitude(),  positionRequest.getLongitude(), SUCCESS_RADIUS_KM)) {
            myTreasureHuntPlayDto.setErrorMessage("Current Position isn't within Radius of Treasure Hunt Posting (1.5m)");
            return myTreasureHuntPlayDto;
        }

        treasureHuntPost.setTermination(true);
        treasureHuntPostRepository.save(treasureHuntPost);
        treasureHuntPostRepository.flush();

        List<MyTreasureHunt> myTreasureHunts = treasureHuntPost.getMyTreasureHunts();

        for (MyTreasureHunt myTreasureHunt : myTreasureHunts) {
            myTreasureHunt.setStatus(PostStatus.TERMINATED);
        }

        MyTreasureHuntPlay myTreasureHuntPlay = myTreasureHuntPlayRepository.findByAppUserAndTreasureHuntPost(appUser, treasureHuntPost);
        myTreasureHuntPlay.setPlayStatus(PlayStatus.CLEARED);
        myTreasureHuntPlayRepository.save(myTreasureHuntPlay);

        return myTreasureHuntPlayToMyTreasureHuntPlayDto(myTreasureHuntPlay);
    }

    @Override
    @Transactional
    public MyTreasureHuntPlayDto changeToAvailableTreasureHuntPlay(Authentication authentication, PositionRequest positionRequest) {
        // 다른 누군가가 완료하지 않아서 종료되지 않았으며 목적지 반경에 들었는가를 확인후 MyTreasureHuntplay 생성
        MyTreasureHuntPlayDto myTreasureHuntPlayDto = new MyTreasureHuntPlayDto();

        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty()) {
            myTreasureHuntPlayDto.setErrorMessage("AppUser Not Found");
            return myTreasureHuntPlayDto;
        }

        AppUser appUser = optionalAppUser.get();

        TreasureHuntPost treasureHuntPost = treasureHuntPostRepository.findById(positionRequest.getTreasureHuntPostId()).orElse(null);

        if (treasureHuntPost == null) {
            myTreasureHuntPlayDto.setErrorMessage("Already Deleted and Not Found"); // 삭제되거나 없는 포스트
            return myTreasureHuntPlayDto;
        }

        if (treasureHuntPost.isTermination()) {
            myTreasureHuntPlayDto.setErrorMessage("Already Terminated Treasure Hunt Posting");
            return myTreasureHuntPlayDto;
        }

        if (!GeoUtil.isWithinRadius(treasureHuntPost.getTreasureHuntStartLocation().getLatitude(), treasureHuntPost.getTreasureHuntStartLocation().getLongitude(), positionRequest.getLatitude(),  positionRequest.getLongitude(), BASE_RADIUS_KM)) {
            myTreasureHuntPlayDto.setErrorMessage("Current Position isn't within Radius of Treasure Hunt Posting (1km)");
            return myTreasureHuntPlayDto;
        }

        MyTreasureHuntPlay myTreasureHuntPlay = new MyTreasureHuntPlay();
        myTreasureHuntPlay.setAppUser(appUser);
        myTreasureHuntPlay.setTreasureHuntPost(treasureHuntPost);
        myTreasureHuntPlay.setPlayStatus(PlayStatus.AVAILABLE);
        myTreasureHuntPlayRepository.save(myTreasureHuntPlay);

        return myTreasureHuntPlayToMyTreasureHuntPlayDto(myTreasureHuntPlay);
    }

    @Override
    @Transactional
    public MyTreasureHuntPlayDto changeToSpecificStatusTreasureHuntPlay(Authentication authentication, PositionRequest positionRequest, PlayStatus playStatus) {
        MyTreasureHuntPlayDto myTreasureHuntPlayDto = new MyTreasureHuntPlayDto();

        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty()) {
            myTreasureHuntPlayDto.setErrorMessage("AppUser Not Found");
            return myTreasureHuntPlayDto;
        }

        AppUser appUser = optionalAppUser.get();

        TreasureHuntPost treasureHuntPost = treasureHuntPostRepository.findById(positionRequest.getTreasureHuntPostId()).orElse(null);

        if (treasureHuntPost == null) {
            myTreasureHuntPlayDto.setErrorMessage("Already Deleted and Not Found"); // 삭제되거나 없는 포스트
            return myTreasureHuntPlayDto;
        }

        if (treasureHuntPost.isTermination()) {
            myTreasureHuntPlayDto.setErrorMessage("Already Terminated Treasure Hunt Posting");
            return myTreasureHuntPlayDto;
        }

        MyTreasureHuntPlay myTreasureHuntPlay;

        switch (playStatus) {
            case AVAILABLE:
                if (!GeoUtil.isWithinRadius(treasureHuntPost.getTreasureHuntStartLocation().getLatitude(), treasureHuntPost.getTreasureHuntStartLocation().getLongitude(), positionRequest.getLatitude(),  positionRequest.getLongitude(), BASE_RADIUS_KM)) {
                    myTreasureHuntPlayDto.setErrorMessage("Current Position isn't within Radius of Treasure Hunt Posting (1km)");
                    return myTreasureHuntPlayDto;
                }

                myTreasureHuntPlay = new MyTreasureHuntPlay();
                myTreasureHuntPlay.setAppUser(appUser);
                myTreasureHuntPlay.setTreasureHuntPost(treasureHuntPost);
                myTreasureHuntPlay.setPlayStatus(PlayStatus.AVAILABLE);
                myTreasureHuntPlayRepository.save(myTreasureHuntPlay);

                return myTreasureHuntPlayToMyTreasureHuntPlayDto(myTreasureHuntPlay);

            case PENDING:
                myTreasureHuntPlay = myTreasureHuntPlayRepository.findByAppUserAndTreasureHuntPost(appUser, treasureHuntPost);

                if (myTreasureHuntPlay.getPlayStatus() != PlayStatus.PLAYING) {
                    myTreasureHuntPlayDto.setErrorMessage("Pending is not possible because it is not in the playing state");
                    return myTreasureHuntPlayDto;
                }

                myTreasureHuntPlay.setPlayStatus(PlayStatus.PENDING);
                myTreasureHuntPlayRepository.save(myTreasureHuntPlay);

                return myTreasureHuntPlayToMyTreasureHuntPlayDto(myTreasureHuntPlay);

            case PLAYING:
                myTreasureHuntPlay = myTreasureHuntPlayRepository.findByAppUserAndTreasureHuntPost(appUser, treasureHuntPost);

                if (myTreasureHuntPlay.getPlayStatus() != PlayStatus.PENDING && myTreasureHuntPlay.getPlayStatus() != PlayStatus.AVAILABLE) {
                    myTreasureHuntPlayDto.setErrorMessage("Playing state is not possible because it is not in the available state or the pending state");
                    return myTreasureHuntPlayDto;
                }

                myTreasureHuntPlay.setPlayStatus(PlayStatus.PLAYING);
                myTreasureHuntPlayRepository.save(myTreasureHuntPlay);

                return myTreasureHuntPlayToMyTreasureHuntPlayDto(myTreasureHuntPlay);

            case CLEARED:
                if (!GeoUtil.isWithinRadius(treasureHuntPost.getTreasureHuntStartLocation().getLatitude(), treasureHuntPost.getTreasureHuntStartLocation().getLongitude(), positionRequest.getLatitude(),  positionRequest.getLongitude(), SUCCESS_RADIUS_KM)) {
                    myTreasureHuntPlayDto.setErrorMessage("Current Position isn't within Radius of Treasure Hunt Posting (1.5m)");
                    return myTreasureHuntPlayDto;
                }

                List<MyTreasureHunt> myTreasureHunts = treasureHuntPost.getMyTreasureHunts();

                for (MyTreasureHunt myTreasureHunt : myTreasureHunts) {
                    if (myTreasureHunt.getStatus() != PostStatus.CREATED)
                        myTreasureHunt.setStatus(PostStatus.TERMINATED);
                }

                myTreasureHuntPlay = myTreasureHuntPlayRepository.findByAppUserAndTreasureHuntPost(appUser, treasureHuntPost);

                if (myTreasureHuntPlay.getPlayStatus() != PlayStatus.PLAYING) {
                    myTreasureHuntPlayDto.setErrorMessage("You are not in the playing state");
                    return myTreasureHuntPlayDto;
                }

                treasureHuntPost.setTermination(true);
                treasureHuntPostRepository.save(treasureHuntPost);
                treasureHuntPostRepository.flush();

                myTreasureHuntPlay.setPlayStatus(PlayStatus.CLEARED);
                myTreasureHuntPlayRepository.save(myTreasureHuntPlay);

                return myTreasureHuntPlayToMyTreasureHuntPlayDto(myTreasureHuntPlay);

            default:
                myTreasureHuntPlayDto.setErrorMessage("Wrong Status Setting (Sever Error)");
                return myTreasureHuntPlayDto;
        }
    }

    @Override
    public MyTreasureHuntPlayDto changeToPlayingTreasureHuntPlay(Authentication authentication, PositionRequest positionRequest) {
        return null;
    }

    @Override
    public MyTreasureHuntPlayDto changeToClearedTreasureHuntPlay(Authentication authentication) {
        // 클리어시 treasureHuntPost termination을 true로 하고 참여자 전부 MyTreasureHunt의 status를 Terminated로 바꿈
        return null;
    }

    @Override
    public List<MyTreasureHuntPlayDto> getAvailableTreasureHuntPlays(Authentication authentication) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();

        List<MyTreasureHuntPlay> myTreasureHuntPlays = myTreasureHuntPlayRepository.findByAppUserAndPlayStatus(appUser, PlayStatus.AVAILABLE);
        List<MyTreasureHuntPlay> excludeTerminatedTreasureHuntPost = new ArrayList<>();

        for (MyTreasureHuntPlay myTreasureHuntPlay : myTreasureHuntPlays) {
            if (!myTreasureHuntPlay.getTreasureHuntPost().isTermination()) {
                excludeTerminatedTreasureHuntPost.add(myTreasureHuntPlay);
            }
        }

        return excludeTerminatedTreasureHuntPost.stream().map(MyTreasureHuntPlayServiceJpaImpl::myTreasureHuntPlayToMyTreasureHuntPlayDto).collect(Collectors.toList());
    }

    @Override
    public List<MyTreasureHuntPlayDto> getSpecificStatusTreasureHuntPlays(Authentication authentication, PlayStatus playStatus) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();

        List<MyTreasureHuntPlay> myTreasureHuntPlays;
        List<MyTreasureHuntPlay> excludeTerminatedTreasureHuntPost = new ArrayList<>();

        switch (playStatus) {
            case AVAILABLE:
                myTreasureHuntPlays = myTreasureHuntPlayRepository.findByAppUserAndPlayStatus(appUser, PlayStatus.AVAILABLE);

                for (MyTreasureHuntPlay myTreasureHuntPlay : myTreasureHuntPlays) {
                    if (!myTreasureHuntPlay.getTreasureHuntPost().isTermination()) {
                        excludeTerminatedTreasureHuntPost.add(myTreasureHuntPlay);
                    }
                }

                return excludeTerminatedTreasureHuntPost.stream().map(MyTreasureHuntPlayServiceJpaImpl::myTreasureHuntPlayToMyTreasureHuntPlayDto).collect(Collectors.toList());

            case PENDING:
                myTreasureHuntPlays = myTreasureHuntPlayRepository.findByAppUserAndPlayStatus(appUser, PlayStatus.PENDING);

                for (MyTreasureHuntPlay myTreasureHuntPlay : myTreasureHuntPlays) {
                    if (!myTreasureHuntPlay.getTreasureHuntPost().isTermination()) {
                        excludeTerminatedTreasureHuntPost.add(myTreasureHuntPlay);
                    }
                }

                return excludeTerminatedTreasureHuntPost.stream().map(MyTreasureHuntPlayServiceJpaImpl::myTreasureHuntPlayToMyTreasureHuntPlayDto).collect(Collectors.toList());

            case PLAYING:
                myTreasureHuntPlays = myTreasureHuntPlayRepository.findByAppUserAndPlayStatus(appUser, PlayStatus.PLAYING);

                for (MyTreasureHuntPlay myTreasureHuntPlay : myTreasureHuntPlays) {
                    if (!myTreasureHuntPlay.getTreasureHuntPost().isTermination()) {
                        excludeTerminatedTreasureHuntPost.add(myTreasureHuntPlay);
                    }
                }

                return excludeTerminatedTreasureHuntPost.stream().map(MyTreasureHuntPlayServiceJpaImpl::myTreasureHuntPlayToMyTreasureHuntPlayDto).collect(Collectors.toList());

            case CLEARED:
                myTreasureHuntPlays = myTreasureHuntPlayRepository.findByAppUserAndPlayStatus(appUser, PlayStatus.CLEARED);
                return myTreasureHuntPlays.stream().map(MyTreasureHuntPlayServiceJpaImpl::myTreasureHuntPlayToMyTreasureHuntPlayDto).collect(Collectors.toList());

            default:
                return null;
        }
    }

    public static MyTreasureHuntPlayDto myTreasureHuntPlayToMyTreasureHuntPlayDto(MyTreasureHuntPlay play) {
        return new MyTreasureHuntPlayDto(
            play.getMyTreasureHuntPlayId(),
            play.getAppUser().getAppUserId(),
            play.getTreasureHuntPost().getTreasureHuntPostId(),
            play.getPlayStatus(),
            play.getCreatedAt(),
            play.getUpdatedAt(),
            null
        );
    }
}
