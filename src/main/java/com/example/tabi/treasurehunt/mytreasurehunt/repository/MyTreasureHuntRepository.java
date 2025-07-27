package com.example.tabi.treasurehunt.mytreasurehunt.repository;

import com.example.tabi.treasurehunt.mytreasurehunt.entity.MyTreasureHunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyTreasureHuntRepository extends JpaRepository<MyTreasureHunt, Long> {
}
