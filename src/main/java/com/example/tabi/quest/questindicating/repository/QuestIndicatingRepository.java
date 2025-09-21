package com.example.tabi.quest.questindicating.repository;

import com.example.tabi.quest.questindicating.entity.QuestIndicating;
import com.example.tabi.quest.queststep.vo.QuestStepDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestIndicatingRepository extends JpaRepository<QuestIndicating, Long> {
}
