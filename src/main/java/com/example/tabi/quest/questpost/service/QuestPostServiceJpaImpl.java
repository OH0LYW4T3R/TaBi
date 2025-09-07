package com.example.tabi.quest.questpost.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.repository.AppUserRepository;
import com.example.tabi.appuser.service.AppUserServiceJpaImpl;
import com.example.tabi.member.repository.MemberRepository;
import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.quest.questpost.repository.QuestPostRepository;
import com.example.tabi.quest.questpost.vo.QuestPostDto;
import com.example.tabi.quest.questpost.vo.QuestPostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestPostServiceJpaImpl implements QuestPostService {
    private final QuestPostRepository questPostRepository;
    private final AppUserRepository appUserRepository;
    private final MemberRepository memberRepository;

    @Override
    public QuestPostDto createQuestPost(Authentication authentication, QuestPostRequest questPostRequest) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();

        QuestPost questPost = new QuestPost();
        questPost.setUploadUserName(appUser.getMyProfile().getNickName());
        questPost.setUploadUserProfileUrl(appUser.getMyProfile().getProfileImageUrl());
//        questPost.setQuestTitle();
//        questPost.setQuestDescription();
//
//        questPost.setLocked(false);
//        questPost.setPub();



        return null;
    }
}
