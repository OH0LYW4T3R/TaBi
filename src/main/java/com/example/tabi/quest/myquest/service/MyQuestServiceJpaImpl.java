package com.example.tabi.quest.myquest.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.quest.myquest.entity.MyQuest;
import com.example.tabi.quest.myquest.repository.MyQuestRepository;
import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.util.PostStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyQuestServiceJpaImpl implements MyQuestService {
    private final MyQuestRepository myQuestRepository;

    @Override
    public void createMyQuest(AppUser appUser, QuestPost questPost) {
        MyQuest myQuest = new MyQuest();
        myQuest.setAppUser(appUser);
        myQuest.setQuestPost(questPost);
        myQuest.setStatus(PostStatus.CREATED);

        myQuestRepository.save(myQuest);
    }
}
