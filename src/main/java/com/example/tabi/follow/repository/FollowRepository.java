package com.example.tabi.follow.repository;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.follow.entity.Follow;
import com.example.tabi.follow.vo.FollowStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerAndFolloweeAndFollowStatus(AppUser follower, AppUser followee, FollowStatus status);
    Optional<Follow> findByFollowerAndFollowee(AppUser follower, AppUser followee);
    List<Follow> findByFollowerAndFollowStatus(AppUser follower, FollowStatus status);
    List<Follow> findByFolloweeAndFollowStatus(AppUser followee, FollowStatus status);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("""
        DELETE FROM Follow f
        WHERE f.followStatus = :status
          AND f.createdAt < :cutoff
    """)
    int deleteAllWithStatusBefore(@Param("status") FollowStatus status, @Param("cutoff") LocalDateTime cutoff);
}
