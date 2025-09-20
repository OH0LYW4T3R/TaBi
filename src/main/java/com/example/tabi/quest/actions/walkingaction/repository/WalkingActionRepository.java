package com.example.tabi.quest.actions.walkingaction.repository;

import com.example.tabi.quest.actions.walkingaction.entity.WalkingAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalkingActionRepository extends JpaRepository<WalkingAction, Long> {
}
