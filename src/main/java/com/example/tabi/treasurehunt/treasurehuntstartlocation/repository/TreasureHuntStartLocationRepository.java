package com.example.tabi.treasurehunt.treasurehuntstartlocation.repository;

import com.example.tabi.treasurehunt.treasurehuntstartlocation.entity.TreasureHuntStartLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreasureHuntStartLocationRepository extends JpaRepository<TreasureHuntStartLocation, Long> {
}
