package com.example.tabi.quest.questrunninglocation.repository;

import com.example.tabi.quest.questrunninglocation.entity.QuestRunningLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestRunningLocationRepository extends JpaRepository<QuestRunningLocation, Long> {
}
