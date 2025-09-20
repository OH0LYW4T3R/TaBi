package com.example.tabi.quest.actions.talkingaction.repository;

import com.example.tabi.quest.actions.talkingaction.entity.TalkingAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TalkingActionRepository extends JpaRepository<TalkingAction, Long> {
}
