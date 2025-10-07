package com.example.tabi.quest.actions.action.repository;

import com.example.tabi.quest.actions.action.entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {
}
