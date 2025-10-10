package com.example.tabi.quest.questpostcomment.repository;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.quest.quest.entity.Quest;
import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.quest.questpostcomment.entity.QuestPostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestPostCommentRepository extends JpaRepository<QuestPostComment, Long> {
    Page<QuestPostComment> findByQuestPostAndParentIsNullOrderByLikeCountDescCreatedAtDesc(QuestPost questPost, Pageable pageable);
    Page<QuestPostComment> findByParentOrderByLikeCountDescCreatedAtDesc(QuestPostComment parent, Pageable pageable);
    Optional<QuestPostComment> findByQuestPostCommentIdAndAppUser(Long questPostCommentId, AppUser appUser);
}
