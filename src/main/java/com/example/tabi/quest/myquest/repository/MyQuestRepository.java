package com.example.tabi.quest.myquest.repository;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.quest.myquest.entity.MyQuest;
import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.quest.questpost.vo.QuestPostDto;
import com.example.tabi.treasurehunt.mytreasurehunt.entity.MyTreasureHunt;
import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import com.example.tabi.util.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyQuestRepository extends JpaRepository<MyQuest, Long> {
    List<MyQuest> findByAppUserAndStatus(AppUser appUser, PostStatus status);
    Optional<MyQuest> findByAppUserAndQuestPost(AppUser appUser, QuestPost questPost);
}
