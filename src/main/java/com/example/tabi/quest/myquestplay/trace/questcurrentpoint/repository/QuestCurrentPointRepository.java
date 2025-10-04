package com.example.tabi.quest.myquestplay.trace.questcurrentpoint.repository;

import com.example.tabi.quest.myquestplay.trace.questcurrentpoint.entity.QuestCurrentPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestCurrentPointRepository extends JpaRepository<QuestCurrentPoint, Long> {
}
