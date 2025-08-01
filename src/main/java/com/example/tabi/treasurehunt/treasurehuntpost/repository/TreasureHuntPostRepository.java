package com.example.tabi.treasurehunt.treasurehuntpost.repository;

import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreasureHuntPostRepository extends JpaRepository<TreasureHuntPost, Long> {
    @Query("""
        SELECT p FROM TreasureHuntPost p
        WHERE p.pub = true
          AND p.locked = false
          AND p.termination = false
          AND p.uploadUserName <> :excludedUsername
    """)
    Page<TreasureHuntPost> findVisiblePostsNotCreatedBy(@Param("excludedUsername") String excludedUsername, Pageable pageable);
}
