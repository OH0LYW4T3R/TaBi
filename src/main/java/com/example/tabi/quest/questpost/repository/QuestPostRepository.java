package com.example.tabi.quest.questpost.repository;

import com.example.tabi.quest.questpost.entity.QuestPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestPostRepository extends JpaRepository<QuestPost, Long> {
    @Query("""
        SELECT p FROM QuestPost p
        WHERE p.pub = true
         AND p.locked = false
         AND p.uploadUserName <> :excludedUsername
    """)
    Page<QuestPost> findVisiblePostsNotCreatedBy(@Param("excludedUsername") String excludedUsername, Pageable pageable);
}
