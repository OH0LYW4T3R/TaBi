package com.example.tabi.quest.myquestplay.trace.questhintsave.repository;

import com.example.tabi.quest.myquestplay.trace.questhintsave.entity.QuestHintSave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestHintSaveRepository extends JpaRepository<QuestHintSave, Long> {
}
