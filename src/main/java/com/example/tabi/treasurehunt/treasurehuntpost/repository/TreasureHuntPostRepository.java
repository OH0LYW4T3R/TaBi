package com.example.tabi.treasurehunt.treasurehuntpost.repository;

import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreasureHuntPostRepository extends JpaRepository<TreasureHuntPost, Long> {
}
