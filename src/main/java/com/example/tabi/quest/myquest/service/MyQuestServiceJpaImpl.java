package com.example.tabi.quest.myquest.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.repository.AppUserRepository;
import com.example.tabi.appuser.service.AppUserServiceJpaImpl;
import com.example.tabi.member.repository.MemberRepository;
import com.example.tabi.postcounter.entity.PostCounter;
import com.example.tabi.quest.myquest.entity.MyQuest;
import com.example.tabi.quest.myquest.repository.MyQuestRepository;
import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.quest.questpost.service.QuestPostServiceJpaImpl;
import com.example.tabi.quest.questpost.vo.QuestPostDto;
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
    private final AppUserRepository appUserRepository;
    private final MemberRepository memberRepository;

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
    public List<QuestPostDto> getRunningStatusQuestPosts(Authentication authentication) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();

        return myQuestRepository.findByAppUserAndStatus(appUser, PostStatus.RUNNING).stream().map(MyQuest::getQuestPost).map(QuestPostDto::questPostToQuestPostDto).toList();
    }
}
