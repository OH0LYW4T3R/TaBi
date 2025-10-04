package com.example.tabi.quest.myquestplay.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.repository.AppUserRepository;
import com.example.tabi.appuser.service.AppUserServiceJpaImpl;
import com.example.tabi.member.repository.MemberRepository;
import com.example.tabi.quest.myquestplay.entity.MyQuestPlay;
import com.example.tabi.quest.myquestplay.repository.MyQuestPlayRepository;
import com.example.tabi.quest.myquestplay.trace.questcurrentpoint.entity.QuestCurrentPoint;
import com.example.tabi.quest.myquestplay.trace.questcurrentpoint.service.QuestCurrentPointService;
import com.example.tabi.quest.myquestplay.trace.questsavepoint.entity.QuestSavePoint;
import com.example.tabi.quest.myquestplay.trace.questsavepoint.service.QuestSavePointService;
import com.example.tabi.quest.myquestplay.vo.MyQuestPlayDto;
import com.example.tabi.quest.myquestplay.vo.PositionRequest;
import com.example.tabi.quest.quest.entity.Quest;
import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.quest.questpost.repository.QuestPostRepository;
import com.example.tabi.quest.questrunninglocation.entity.QuestRunningLocation;
import com.example.tabi.quest.questrunninglocation.repository.QuestRunningLocationRepository;
import com.example.tabi.reward.service.RewardService;
import com.example.tabi.treasurehunt.mytreasurehunt.entity.MyTreasureHunt;
import com.example.tabi.treasurehunt.mytreasurehuntplay.entity.MyTreasureHuntPlay;
import com.example.tabi.util.GeoUtil;
import com.example.tabi.util.PlayStatus;
import com.example.tabi.util.PostStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyQuestPlayServiceJpaImpl implements MyQuestPlayService {
    private static final double START_BASE_RADIUS_KM = 0.015;

    private final AppUserRepository appUserRepository;
    private final MemberRepository memberRepository;
    private final MyQuestPlayRepository myQuestPlayRepository;
    private final QuestPostRepository questPostRepository;
    private final QuestRunningLocationRepository questRunningLocationRepository;

    private final RewardService rewardService;

    private QuestCurrentPointService questCurrentPointService;
    private QuestSavePointService questSavePointService;

    @Override
    @Transactional
    public MyQuestPlayDto changeToSpecificStatusQuestPlay(Authentication authentication, PositionRequest positionRequest, PlayStatus playStatus) {
        MyQuestPlayDto myQuestPlayForErrorDto = new MyQuestPlayDto();
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty()) {
            myQuestPlayForErrorDto.setErrorMessage("AppUser Not Found");
            return myQuestPlayForErrorDto;
        }

        AppUser appUser = optionalAppUser.get();

        QuestPost questPost = questPostRepository.findById(positionRequest.getQuestPostId()).orElse(null);

        if (questPost == null) {
            myQuestPlayForErrorDto.setErrorMessage("Already Deleted and Not Found"); // 삭제되거나 없는 포스트
            return myQuestPlayForErrorDto;
        }

        if (!questPost.isPub() || questPost.isLocked()) {
            myQuestPlayForErrorDto.setErrorMessage("It is not Public or Locked"); // 삭제되거나 없는 포스트
            return myQuestPlayForErrorDto;
        }

        Optional<MyQuestPlay> myQuestPlayOptinal = myQuestPlayRepository.findByAppUserAndQuestPost(appUser, questPost);
        MyQuestPlay myQuestPlay;

        switch (playStatus) {
            case AVAILABLE:
                if (myQuestPlayOptinal.isEmpty()) {
                     if (!GeoUtil.isWithinRadius(questPost.getQuestStartLocation().getLatitude(), questPost.getQuestStartLocation().getLongitude(), positionRequest.getLatitude(),  positionRequest.getLongitude(), START_BASE_RADIUS_KM)) {
                        myQuestPlayForErrorDto.setErrorMessage("Current Position isn't within Radius of Quest Start Base Location (15m)");
                        return myQuestPlayForErrorDto;
                    }
                    // 첫 실행인 경우
                    myQuestPlay = new MyQuestPlay();
                    myQuestPlay.setAppUser(appUser);
                    myQuestPlay.setQuestPost(questPost);
                    myQuestPlay.setPlayStatus(PlayStatus.AVAILABLE);

                    myQuestPlayRepository.save(myQuestPlay);

                    return MyQuestPlayDto.myQuestPlayToMyQuestPlayDto(myQuestPlay);
                } else {
                    // 두 번째 실행인 경우 (== Pending에서 온 경우)
                    myQuestPlay = myQuestPlayOptinal.get();
                    if (myQuestPlay.getPlayStatus() != PlayStatus.PENDING) {
                        myQuestPlayForErrorDto.setErrorMessage("Available is not possible because it is not in the pending state");
                        return myQuestPlayForErrorDto;
                    }

                    Optional<QuestRunningLocation> currentQuestRunningLocationOptional = questRunningLocationRepository.findById(myQuestPlay.getQuestCurrentPoint().getCurrentQuestRunningLocationId());

                    if (currentQuestRunningLocationOptional.isEmpty()) {
                        myQuestPlayForErrorDto.setErrorMessage("[Server Error] - QuestRunningLocation Error");
                        return myQuestPlayForErrorDto;
                    }

                    QuestRunningLocation questRunningLocation = currentQuestRunningLocationOptional.get();

                    if (!GeoUtil.isWithinRadius(questRunningLocation.getLatitude(), questRunningLocation.getLongitude(), positionRequest.getLatitude(),  positionRequest.getLongitude(), START_BASE_RADIUS_KM)) {
                        myQuestPlayForErrorDto.setErrorMessage("Current Position isn't within Radius of Quest Start Base Location (15m)");
                        return myQuestPlayForErrorDto;
                    }

                    myQuestPlay.setPlayStatus(PlayStatus.AVAILABLE);
                    myQuestPlayRepository.save(myQuestPlay);

                    return MyQuestPlayDto.myQuestPlayToMyQuestPlayDto(myQuestPlayOptinal.get());
                }

            case PENDING:
                // Quest Current Point의 QuestNextLocationDto를 응답 받으면 해당 위치로 부터 5km 떨어지면 Pending 요청
                if (myQuestPlayOptinal.isEmpty()) {
                    myQuestPlayForErrorDto.setErrorMessage("[Invalid request] - request Available State first");
                    return myQuestPlayForErrorDto;
                }

                myQuestPlay = myQuestPlayOptinal.get();

                if (myQuestPlay.getPlayStatus() != PlayStatus.PLAYING && myQuestPlay.getPlayStatus() != PlayStatus.AVAILABLE) {
                    myQuestPlayForErrorDto.setErrorMessage("Pending is not possible because it is not in the avaliable state or playing state");
                    return myQuestPlayForErrorDto;
                }

                if (myQuestPlay.getQuestSavePoint() == null) {
                    // 첫 실행의 경우
                    QuestCurrentPoint questCurrentPoint;
                    if (myQuestPlay.getQuestCurrentPoint() == null) {
                        // Available에서 온경우
                        questCurrentPoint = questCurrentPointService.createQuestCurrentPoint(myQuestPlay);
                        myQuestPlay.setQuestCurrentPoint(questCurrentPoint);
                    } else {
                        // Play에서 온 경우
                        questCurrentPoint = myQuestPlay.getQuestCurrentPoint();
                    }

                    myQuestPlay.setQuestSavePoint(questSavePointService.createQuestSavePoint(myQuestPlay, questCurrentPoint));
                } else {
                    // 두번째 이상의 실행의 경우
                    questSavePointService.updateQuestSavePoint(myQuestPlay, myQuestPlay.getQuestCurrentPoint());
                }

                myQuestPlay.setPlayStatus(PlayStatus.PENDING);
                myQuestPlayRepository.save(myQuestPlay);

                return MyQuestPlayDto.myQuestPlayToMyQuestPlayDto(myQuestPlay);

            case PLAYING:
                if (myQuestPlayOptinal.isEmpty()) {
                    myQuestPlayForErrorDto.setErrorMessage("[Invalid request] - request Available State first");
                    return myQuestPlayForErrorDto;
                }

                myQuestPlay = myQuestPlayOptinal.get();

                if (myQuestPlay.getPlayStatus() != PlayStatus.PENDING && myQuestPlay.getPlayStatus() != PlayStatus.AVAILABLE) {
                    myQuestPlayForErrorDto.setErrorMessage("Playing state is not possible because it is not in the available state or the pending state");
                    return myQuestPlayForErrorDto;
                }

                if (myQuestPlay.getQuestCurrentPoint() == null)
                    // 첫 플레이시 cur 생성
                    myQuestPlay.setQuestCurrentPoint(questCurrentPointService.createQuestCurrentPoint(myQuestPlay));
                else {
                    // 두번째 이상 플레이시 save data 로드
                    // 두번째 이상 부터는 무조건 Pending State를 최소 한번 거침
                    if (myQuestPlay.getQuestSavePoint() == null) {
                        myQuestPlayForErrorDto.setErrorMessage("Save Point Not Found - Missing Pending Request");
                        return myQuestPlayForErrorDto;
                    }
                    questSavePointService.loadQuestSavePoint(myQuestPlay.getQuestSavePoint(), myQuestPlay.getQuestCurrentPoint());
                }

                myQuestPlay.setPlayStatus(PlayStatus.PLAYING);
                myQuestPlayRepository.save(myQuestPlay);

                return MyQuestPlayDto.myQuestPlayToMyQuestPlayDto(myQuestPlay);

            case CLEARED:
                // cur end 비교 (위치,  액선)
                if (myQuestPlayOptinal.isEmpty()) {
                    myQuestPlayForErrorDto.setErrorMessage("[Invalid request] - request Available State first");
                    return myQuestPlayForErrorDto;
                }

                myQuestPlay = myQuestPlayOptinal.get();
                QuestCurrentPoint questCurrentPoint = myQuestPlay.getQuestCurrentPoint();

                if (!Objects.equals(questCurrentPoint.getCurrentActionIndex(), questCurrentPoint.getEndActionIndex()) && !Objects.equals(questCurrentPoint.getCurrentQuestRunningLocationIndex(), questCurrentPoint.getEndQuestRunningLocationIndex())) {
                    myQuestPlayForErrorDto.setErrorMessage("The quest is not finished and cannot be cleared.");
                    return myQuestPlayForErrorDto;
                }
                //리워드 추가
                // 플레이 상태 변경
                myQuestPlay.setPlayStatus(PlayStatus.CLEARED);
                rewardService.addReward(appUser, questPost.getReward());
                myQuestPlayRepository.save(myQuestPlay);

                return MyQuestPlayDto.myQuestPlayToMyQuestPlayDto(myQuestPlay);

            default:
                myQuestPlayForErrorDto.setErrorMessage("Wrong Status Setting (Sever Error)");
                return myQuestPlayForErrorDto;
        }
    }

    @Override
    public List<MyQuestPlayDto> getSpecificStatusQuestPlays(Authentication authentication, PlayStatus playStatus) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();

        List<MyQuestPlay> myQuestPlays;

        switch (playStatus) {
            case AVAILABLE:
                myQuestPlays = myQuestPlayRepository.findByAppUserAndPlayStatus(appUser, PlayStatus.AVAILABLE);
                return myQuestPlays.stream().map(MyQuestPlayDto::myQuestPlayToMyQuestPlayDto).collect(Collectors.toList());
            case PENDING:
                myQuestPlays = myQuestPlayRepository.findByAppUserAndPlayStatus(appUser, PlayStatus.PENDING);
                return myQuestPlays.stream().map(MyQuestPlayDto::myQuestPlayToMyQuestPlayDto).collect(Collectors.toList());
            case PLAYING:
                myQuestPlays = myQuestPlayRepository.findByAppUserAndPlayStatus(appUser, PlayStatus.PLAYING);
                return myQuestPlays.stream().map(MyQuestPlayDto::myQuestPlayToMyQuestPlayDto).collect(Collectors.toList());
            case CLEARED:
                myQuestPlays = myQuestPlayRepository.findByAppUserAndPlayStatus(appUser, PlayStatus.CLEARED);
                return myQuestPlays.stream().map(MyQuestPlayDto::myQuestPlayToMyQuestPlayDto).collect(Collectors.toList());
            default:
                return null;
        }
    }
}
