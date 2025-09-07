package com.example.tabi.quest.questpost.repository;

import com.example.tabi.quest.questpost.entity.QuestPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestPostRepository extends JpaRepository<QuestPost, Long> {
}
