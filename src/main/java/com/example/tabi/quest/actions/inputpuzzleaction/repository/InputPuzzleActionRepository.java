package com.example.tabi.quest.actions.inputpuzzleaction.repository;

import com.example.tabi.quest.actions.inputpuzzleaction.entity.InputPuzzleAction;
import com.example.tabi.quest.actions.locationpuzzleaction.entity.LocationPuzzleAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InputPuzzleActionRepository extends JpaRepository<InputPuzzleAction, Long> {
}
