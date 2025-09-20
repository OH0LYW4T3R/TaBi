package com.example.tabi.quest.actions.stayingaction.repository;

import com.example.tabi.quest.actions.stayingaction.entity.StayingAction;
import com.example.tabi.quest.actions.talkingaction.entity.TalkingAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StayingActionRepository extends JpaRepository<StayingAction, Long> {
}
