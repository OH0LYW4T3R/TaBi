package com.example.tabi.quest.questpostimage.repository;

import com.example.tabi.quest.questpostimage.entity.QuestPostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestPostImageRepository extends JpaRepository<QuestPostImage, Long> {
}
