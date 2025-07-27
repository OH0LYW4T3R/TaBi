package com.example.tabi.treasurehunt.treasurehuntpostImage.repository;

import com.example.tabi.treasurehunt.treasurehuntpostImage.entity.TreasureHuntPostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreasureHuntPostImageRepository extends JpaRepository<TreasureHuntPostImage, Long> {
}
