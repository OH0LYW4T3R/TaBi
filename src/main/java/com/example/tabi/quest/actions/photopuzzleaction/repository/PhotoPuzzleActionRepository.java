package com.example.tabi.quest.actions.photopuzzleaction.repository;

import com.example.tabi.quest.actions.photopuzzleaction.entity.PhotoPuzzleAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoPuzzleActionRepository extends JpaRepository<PhotoPuzzleAction, Long> {
}
