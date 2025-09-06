package com.example.tabi.quest.myquestplay.repository;

import com.example.tabi.quest.myquest.entity.MyQuest;
import com.example.tabi.quest.myquestplay.entity.MyQuestPlay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyQuestPlayRepository extends JpaRepository<MyQuestPlay,Long> {
}
