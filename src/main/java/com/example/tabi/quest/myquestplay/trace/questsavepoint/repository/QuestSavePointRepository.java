package com.example.tabi.quest.myquestplay.trace.questsavepoint.repository;

import com.example.tabi.quest.myquestplay.trace.questsavepoint.entity.QuestSavePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestSavePointRepository extends JpaRepository<QuestSavePoint, Long> {
}
