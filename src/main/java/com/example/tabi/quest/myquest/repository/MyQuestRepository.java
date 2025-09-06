package com.example.tabi.quest.myquest.repository;

import com.example.tabi.quest.myquest.entity.MyQuest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyQuestRepository extends JpaRepository<MyQuest, Long> {
}
