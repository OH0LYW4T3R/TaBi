package com.example.tabi.quest.actions.locationpuzzleaction.repository;

import com.example.tabi.quest.actions.locationpuzzleaction.entity.LocationPuzzleAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationPuzzleActionRepository extends JpaRepository<LocationPuzzleAction, Long> {
}
