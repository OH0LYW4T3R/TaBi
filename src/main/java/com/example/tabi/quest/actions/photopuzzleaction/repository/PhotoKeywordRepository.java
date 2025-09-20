package com.example.tabi.quest.actions.photopuzzleaction.repository;

import com.example.tabi.quest.actions.photopuzzleaction.entity.PhotoKeyword;
import com.example.tabi.quest.actions.photopuzzleaction.vo.PhotoKeywordRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoKeywordRepository extends JpaRepository<PhotoKeyword, Long> {
}
