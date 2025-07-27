package com.example.tabi.treasurehunt.treasurehuntstartlocation.repository;

import com.example.tabi.treasurehunt.treasurehuntstartlocation.entity.TreasureHuntLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreasureHuntLocationRepository extends JpaRepository<TreasureHuntLocation,Long> {
}
