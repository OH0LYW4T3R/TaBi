package com.example.tabi.treasurehunt.mytreasurehunt.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.repository.AppUserRepository;
import com.example.tabi.appuser.service.AppUserServiceJpaImpl;
import com.example.tabi.member.repository.MemberRepository;
import com.example.tabi.myprofile.entity.MyProfile;
import com.example.tabi.myprofile.repository.MyProfileRepository;
import com.example.tabi.postcounter.entity.PostCounter;
import com.example.tabi.treasurehunt.treasurehuntpost.repository.TreasureHuntPostRepository;
import com.example.tabi.util.PostStatus;
import com.example.tabi.treasurehunt.mytreasurehunt.entity.MyTreasureHunt;
import com.example.tabi.treasurehunt.mytreasurehunt.repository.MyTreasureHuntRepository;
import com.example.tabi.treasurehunt.mytreasurehunt.vo.MyTreasureHuntDto;
import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import com.example.tabi.treasurehunt.treasurehuntpost.service.TreasureHuntPostServiceJpaImpl;
import com.example.tabi.treasurehunt.treasurehuntpost.vo.TreasureHuntPostDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyTreasureHuntServiceJpaImpl implements MyTreasureHuntService {
    private final MyTreasureHuntRepository myTreasureHuntRepository;
    private final AppUserRepository appUserRepository;
    private final MemberRepository memberRepository;
    private final TreasureHuntPostRepository treasureHuntPostRepository;
    private final MyProfileRepository myProfileRepository;

    @Override
    @Transactional
    public void createMyTreasureHunt(AppUser appUser, TreasureHuntPost treasureHuntPost) {
        MyTreasureHunt myTreasureHunt = new MyTreasureHunt();
        myTreasureHunt.setAppUser(appUser);
        myTreasureHunt.setTreasureHuntPost(treasureHuntPost);
        myTreasureHunt.setStatus(PostStatus.CREATED);

        myTreasureHuntRepository.save(myTreasureHunt);
    }

    @Override
    @Transactional
    public void playMyTreasureHunt(AppUser appUser, TreasureHuntPost treasureHuntPost) {
        MyTreasureHunt myTreasureHunt = new MyTreasureHunt();
        myTreasureHunt.setAppUser(appUser);
        myTreasureHunt.setTreasureHuntPost(treasureHuntPost);
        myTreasureHunt.setStatus(PostStatus.RUNNING);

        PostCounter postCounter = treasureHuntPost.getPostCounter();
        postCounter.setPlayCount(postCounter.getPlayCount() + 1);

        myTreasureHuntRepository.save(myTreasureHunt);
    }

    @Override
    @Transactional
    public TreasureHuntPostDto saveMyTreasureHunt(Authentication authentication, Long treasureHuntPostId) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);
        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();

        TreasureHuntPost treasureHuntPost = treasureHuntPostRepository.findById(treasureHuntPostId).orElse(null);
        if (treasureHuntPost == null)
            return null;

        Optional<MyTreasureHunt> optionalMyTreasureHunt = myTreasureHuntRepository.findByAppUserAndTreasureHuntPost(appUser, treasureHuntPost);

        if (optionalMyTreasureHunt.isPresent()) {
            return null;
        }

        MyTreasureHunt newMyTreasureHunt = new MyTreasureHunt();
        newMyTreasureHunt.setAppUser(appUser);
        newMyTreasureHunt.setTreasureHuntPost(treasureHuntPost);
        newMyTreasureHunt.setStatus(PostStatus.SAVED);
        myTreasureHuntRepository.save(newMyTreasureHunt);

        return TreasureHuntPostServiceJpaImpl.treasureHuntPostToTreasureHuntPostDto(
                treasureHuntPost,
                treasureHuntPost.getPostCounter(),
                treasureHuntPost.getReward(),
                treasureHuntPost.getTreasureHuntStartLocation(),
                treasureHuntPost.getTreasureHuntPostImages()
        );
    }

    @Override
    public List<TreasureHuntPostDto> getCreatedStatusTreasureHuntPosts(Authentication authentication) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);
        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();

        return myTreasureHuntRepository.findByAppUserAndStatus(appUser, PostStatus.CREATED)
                .stream()
                .map(treasureHunt -> TreasureHuntPostServiceJpaImpl.treasureHuntPostToTreasureHuntPostDto(
                        treasureHunt.getTreasureHuntPost(),
                        treasureHunt.getTreasureHuntPost().getPostCounter(),
                        treasureHunt.getTreasureHuntPost().getReward(),
                        treasureHunt.getTreasureHuntPost().getTreasureHuntStartLocation(),
                        treasureHunt.getTreasureHuntPost().getTreasureHuntPostImages()
                ))
                .toList();
    }

    @Override
    public List<TreasureHuntPostDto> getSavedStatusTreasureHuntPosts(Authentication authentication) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);
        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();

        return myTreasureHuntRepository.findByAppUserAndStatus(appUser, PostStatus.SAVED)
                .stream()
                .map(treasureHunt -> TreasureHuntPostServiceJpaImpl.treasureHuntPostToTreasureHuntPostDto(
                        treasureHunt.getTreasureHuntPost(),
                        treasureHunt.getTreasureHuntPost().getPostCounter(),
                        treasureHunt.getTreasureHuntPost().getReward(),
                        treasureHunt.getTreasureHuntPost().getTreasureHuntStartLocation(),
                        treasureHunt.getTreasureHuntPost().getTreasureHuntPostImages()
                ))
                .toList();
    }

    @Override
    public List<TreasureHuntPostDto> getTerminatedStatusTreasureHuntPosts(Authentication authentication) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);
        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();

        return myTreasureHuntRepository.findByAppUserAndStatus(appUser, PostStatus.TERMINATED)
                .stream()
                .map(treasureHunt -> TreasureHuntPostServiceJpaImpl.treasureHuntPostToTreasureHuntPostDto(
                        treasureHunt.getTreasureHuntPost(),
                        treasureHunt.getTreasureHuntPost().getPostCounter(),
                        treasureHunt.getTreasureHuntPost().getReward(),
                        treasureHunt.getTreasureHuntPost().getTreasureHuntStartLocation(),
                        treasureHunt.getTreasureHuntPost().getTreasureHuntPostImages()
                ))
                .toList();
    }

    @Override
    @Transactional
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
    public List<TreasureHuntPostDto> getCreatedStatusTreasureHuntPostsForCounterparty(Long myProfileId) {
        Optional<MyProfile> optionalMyProfile = myProfileRepository.findById(myProfileId);

        if (optionalMyProfile.isEmpty())
            return null;

        MyProfile myProfile = optionalMyProfile.get();
        AppUser counterpartyAppUser = myProfile.getAppUser();


        return myTreasureHuntRepository.findByAppUserAndStatus(counterpartyAppUser, PostStatus.CREATED).stream().map(
                treasureHunt -> TreasureHuntPostServiceJpaImpl.treasureHuntPostToTreasureHuntPostDto(
                        treasureHunt.getTreasureHuntPost(),
                        treasureHunt.getTreasureHuntPost().getPostCounter(),
                        treasureHunt.getTreasureHuntPost().getReward(),
                        treasureHunt.getTreasureHuntPost().getTreasureHuntStartLocation(),
                        treasureHunt.getTreasureHuntPost().getTreasureHuntPostImages()
                )).toList();
    }

    @Override
    public List<TreasureHuntPostDto> getSavedStatusTreasureHuntPostsForCounterparty(Long myProfileId) {
        Optional<MyProfile> optionalMyProfile = myProfileRepository.findById(myProfileId);

        if (optionalMyProfile.isEmpty())
            return null;

        MyProfile myProfile = optionalMyProfile.get();
        AppUser counterpartyAppUser = myProfile.getAppUser();


        return myTreasureHuntRepository.findByAppUserAndStatus(counterpartyAppUser, PostStatus.SAVED).stream().map(
                treasureHunt -> TreasureHuntPostServiceJpaImpl.treasureHuntPostToTreasureHuntPostDto(
                        treasureHunt.getTreasureHuntPost(),
                        treasureHunt.getTreasureHuntPost().getPostCounter(),
                        treasureHunt.getTreasureHuntPost().getReward(),
                        treasureHunt.getTreasureHuntPost().getTreasureHuntStartLocation(),
                        treasureHunt.getTreasureHuntPost().getTreasureHuntPostImages()
                )).toList();
    }

    @Override
    public List<TreasureHuntPostDto> getTerminatedStatusTreasureHuntPostsForCounterparty(Long myProfileId) {
        Optional<MyProfile> optionalMyProfile = myProfileRepository.findById(myProfileId);

        if (optionalMyProfile.isEmpty())
            return null;

        MyProfile myProfile = optionalMyProfile.get();
        AppUser counterpartyAppUser = myProfile.getAppUser();


        return myTreasureHuntRepository.findByAppUserAndStatus(counterpartyAppUser, PostStatus.TERMINATED).stream().map(
                treasureHunt -> TreasureHuntPostServiceJpaImpl.treasureHuntPostToTreasureHuntPostDto(
                        treasureHunt.getTreasureHuntPost(),
                        treasureHunt.getTreasureHuntPost().getPostCounter(),
                        treasureHunt.getTreasureHuntPost().getReward(),
                        treasureHunt.getTreasureHuntPost().getTreasureHuntStartLocation(),
                        treasureHunt.getTreasureHuntPost().getTreasureHuntPostImages()
                )).toList();
    }

    @Override
    public List<TreasureHuntPostDto> getRunningStatusTreasureHuntPostsForCounterparty(Long myProfileId) {
        Optional<MyProfile> optionalMyProfile = myProfileRepository.findById(myProfileId);

        if (optionalMyProfile.isEmpty())
            return null;

        MyProfile myProfile = optionalMyProfile.get();
        AppUser counterpartyAppUser = myProfile.getAppUser();


        return myTreasureHuntRepository.findByAppUserAndStatus(counterpartyAppUser, PostStatus.RUNNING).stream().map(
                treasureHunt -> TreasureHuntPostServiceJpaImpl.treasureHuntPostToTreasureHuntPostDto(
                        treasureHunt.getTreasureHuntPost(),
                        treasureHunt.getTreasureHuntPost().getPostCounter(),
                        treasureHunt.getTreasureHuntPost().getReward(),
                        treasureHunt.getTreasureHuntPost().getTreasureHuntStartLocation(),
                        treasureHunt.getTreasureHuntPost().getTreasureHuntPostImages()
                )).toList();
    }

    public static MyTreasureHuntDto myTreasureHuntToMyTreasureHuntDto(MyTreasureHunt myTreasureHunt) {
        return new MyTreasureHuntDto(myTreasureHunt.getMyTreasureHuntId(), null, myTreasureHunt.getStatus(), myTreasureHunt.getCreatedAt());
    }
}
