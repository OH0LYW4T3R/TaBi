package com.example.tabi.treasurehunt.treasurehuntpostcomment.repository;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import com.example.tabi.treasurehunt.treasurehuntpostcomment.entity.TreasureHuntPostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TreasureHuntPostCommentRepository extends JpaRepository<TreasureHuntPostComment, Long> {
    // 게시물의 최상위 댓글(좋아요 desc, 최신 desc)
    Page<TreasureHuntPostComment> findByTreasureHuntPostAndParentIsNullOrderByLikeCountDescCreatedAtDesc(TreasureHuntPost post, Pageable pageable);

    // 특정 부모 댓글의 대댓글(좋아요 desc, 최신 desc)
    Page<TreasureHuntPostComment> findByParentOrderByLikeCountDescCreatedAtDesc(TreasureHuntPostComment parent, Pageable pageable);

    Optional<TreasureHuntPostComment> findByTreasureHuntPostCommentIdAndAppUser(Long id, AppUser appUser);

    // 필요 시: 한 번에 children size만 알고 싶을 때
}
