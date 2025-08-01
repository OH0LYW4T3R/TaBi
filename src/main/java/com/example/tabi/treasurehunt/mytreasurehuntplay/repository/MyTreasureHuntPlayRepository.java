package com.example.tabi.treasurehunt.mytreasurehuntplay.repository;

import com.example.tabi.treasurehunt.mytreasurehuntplay.entity.MyTreasureHuntPlay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyTreasureHuntPlayRepository extends JpaRepository<MyTreasureHuntPlay, Long> {
}
