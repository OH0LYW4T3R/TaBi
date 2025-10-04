package com.example.tabi.quest.myquestplay.repository;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.quest.myquest.entity.MyQuest;
import com.example.tabi.quest.myquestplay.entity.MyQuestPlay;
import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.util.PlayStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyQuestPlayRepository extends JpaRepository<MyQuestPlay,Long> {
    Optional<MyQuestPlay> findByAppUserAndQuestPost(AppUser appUser, QuestPost questPost);
    List<MyQuestPlay> findByAppUserAndPlayStatus(AppUser appUser, PlayStatus playStatus);
}
