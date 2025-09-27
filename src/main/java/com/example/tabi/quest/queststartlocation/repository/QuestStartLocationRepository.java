package com.example.tabi.quest.queststartlocation.repository;

import com.example.tabi.quest.queststartlocation.entity.QuestStartLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestStartLocationRepository extends JpaRepository<QuestStartLocation, Long> {
}
