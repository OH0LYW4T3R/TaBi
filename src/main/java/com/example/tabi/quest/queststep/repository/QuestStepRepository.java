package com.example.tabi.quest.queststep.repository;

import com.example.tabi.quest.queststep.entity.QuestStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestStepRepository extends JpaRepository<QuestStep, Long> {
}
