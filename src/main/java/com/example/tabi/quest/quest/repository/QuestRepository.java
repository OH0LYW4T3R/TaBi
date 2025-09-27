package com.example.tabi.quest.quest.repository;

import com.example.tabi.quest.quest.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Long> {
}
