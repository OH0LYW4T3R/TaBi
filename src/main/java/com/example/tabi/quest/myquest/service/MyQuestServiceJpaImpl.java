package com.example.tabi.quest.myquest.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.repository.AppUserRepository;
import com.example.tabi.appuser.service.AppUserServiceJpaImpl;
import com.example.tabi.member.repository.MemberRepository;
import com.example.tabi.myprofile.entity.MyProfile;
import com.example.tabi.myprofile.repository.MyProfileRepository;
import com.example.tabi.postcounter.entity.PostCounter;
import com.example.tabi.quest.myquest.entity.MyQuest;
import com.example.tabi.quest.myquest.repository.MyQuestRepository;
import com.example.tabi.quest.myquestplay.entity.MyQuestPlay;
import com.example.tabi.quest.myquestplay.repository.MyQuestPlayRepository;
import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.quest.questpost.repository.QuestPostRepository;
import com.example.tabi.quest.questpost.service.QuestPostServiceJpaImpl;
import com.example.tabi.quest.questpost.vo.QuestPostDto;
import com.example.tabi.util.PlayStatus;
import com.example.tabi.util.PostStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyQuestServiceJpaImpl implements MyQuestService {
    private final MyQuestRepository myQuestRepository;
    private final MyQuestPlayRepository myQuestPlayRepository;
    private final AppUserRepository appUserRepository;
    private final MemberRepository memberRepository;
    private final QuestPostRepository questPostRepository;
    private final MyProfileRepository myProfileRepository;

    @Override
    public void createMyQuest(AppUser appUser, QuestPost questPost) {
        MyQuest myQuest = new MyQuest();
        myQuest.setAppUser(appUser);
        myQuest.setQuestPost(questPost);
        myQuest.setStatus(PostStatus.CREATED);

        myQuestRepository.save(myQuest);
    }

    @Override
    public void playMyQuest(AppUser appUser, QuestPost questPost) {
        MyQuest myQuest = new MyQuest();
        myQuest.setAppUser(appUser);
        myQuest.setQuestPost(questPost);
        myQuest.setStatus(PostStatus.RUNNING);

        PostCounter postCounter = questPost.getPostCounter();
        postCounter.setPlayCount(postCounter.getPlayCount() + 1);

        myQuestRepository.save(myQuest);
    }

    @Override
    public QuestPostDto saveMyQuest(Authentication authentication, Long questPostId) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);
        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();

        QuestPost questPost = questPostRepository.findById(questPostId).orElse(null);
        if (questPost == null)
            return null;

        Optional<MyQuest> optionalMyQuest = myQuestRepository.findByAppUserAndQuestPost(appUser, questPost);

        if (optionalMyQuest.isPresent())
            return null;

        MyQuest newMyQuest = new MyQuest();
        newMyQuest.setAppUser(appUser);
        newMyQuest.setQuestPost(questPost);
        newMyQuest.setStatus(PostStatus.SAVED);
        myQuestRepository.save(newMyQuest);

        return QuestPostDto.questPostToQuestPostDto(questPost);
    }

    @Override
    public List<QuestPostDto> getCreatedStatusQuestPosts(Authentication authentication) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);
        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();

        return myQuestRepository.findByAppUserAndStatus(appUser, PostStatus.CREATED)
                .stream()
                .map(MyQuest::getQuestPost)
                .map(QuestPostDto::questPostToQuestPostDto)
                .toList();
    }

    @Override
    public List<QuestPostDto> getSavedStatusQuestPosts(Authentication authentication) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);
        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();

        return myQuestRepository.findByAppUserAndStatus(appUser, PostStatus.SAVED)
                .stream()
                .map(MyQuest::getQuestPost)
                .map(QuestPostDto::questPostToQuestPostDto)
                .toList();
    }

    @Override
    public List<QuestPostDto> getTerminatedQuestPosts(Authentication authentication) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);
        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();

        return myQuestPlayRepository.findByAppUserAndPlayStatus(appUser, PlayStatus.CLEARED).stream()
                .map(MyQuestPlay::getQuestPost)
                .map(QuestPostDto::questPostToQuestPostDto)
                .toList();
    }

    @Override
    public List<QuestPostDto> getRunningStatusQuestPosts(Authentication authentication) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();

        return myQuestRepository.findByAppUserAndStatus(appUser, PostStatus.RUNNING).stream().map(MyQuest::getQuestPost).map(QuestPostDto::questPostToQuestPostDto).toList();
    }

    @Override
    public List<QuestPostDto> getCreatedStatusQuestPostsForCounterparty(Long myProfileId) {
        Optional<MyProfile> optionalMyProfile = myProfileRepository.findById(myProfileId);

        if (optionalMyProfile.isEmpty())
            return null;

        MyProfile myProfile = optionalMyProfile.get();
        AppUser counterpartyAppUser = myProfile.getAppUser();

        return myQuestRepository.findByAppUserAndStatus(counterpartyAppUser, PostStatus.CREATED)
                .stream()
                .map(MyQuest::getQuestPost)
                .map(QuestPostDto::questPostToQuestPostDto)
                .toList();
    }

    @Override
    public List<QuestPostDto> getSavedStatusQuestPostsForCounterparty(Long myProfileId) {
        Optional<MyProfile> optionalMyProfile = myProfileRepository.findById(myProfileId);

        if (optionalMyProfile.isEmpty())
            return null;

        MyProfile myProfile = optionalMyProfile.get();
        AppUser counterpartyAppUser = myProfile.getAppUser();

        return myQuestRepository.findByAppUserAndStatus(counterpartyAppUser, PostStatus.SAVED)
                .stream()
                .map(MyQuest::getQuestPost)
                .map(QuestPostDto::questPostToQuestPostDto)
                .toList();
    }

    @Override
    public List<QuestPostDto> getTerminatedStatusQuestPostsForCounterparty(Long myProfileId) {
        Optional<MyProfile> optionalMyProfile = myProfileRepository.findById(myProfileId);

        if (optionalMyProfile.isEmpty())
            return null;

        MyProfile myProfile = optionalMyProfile.get();
        AppUser counterpartyAppUser = myProfile.getAppUser();

        return myQuestPlayRepository.findByAppUserAndPlayStatus(counterpartyAppUser, PlayStatus.CLEARED).stream()
                .map(MyQuestPlay::getQuestPost)
                .map(QuestPostDto::questPostToQuestPostDto)
                .toList();
    }

    @Override
    public List<QuestPostDto> getRunningStatusQuestPostsForCounterparty(Long myProfileId) {
        Optional<MyProfile> optionalMyProfile = myProfileRepository.findById(myProfileId);

        if (optionalMyProfile.isEmpty())
            return null;

        MyProfile myProfile = optionalMyProfile.get();
        AppUser counterpartyAppUser = myProfile.getAppUser();

        return myQuestRepository.findByAppUserAndStatus(counterpartyAppUser, PostStatus.RUNNING)
                .stream()
                .map(MyQuest::getQuestPost)
                .map(QuestPostDto::questPostToQuestPostDto)
                .toList();
    }
}
