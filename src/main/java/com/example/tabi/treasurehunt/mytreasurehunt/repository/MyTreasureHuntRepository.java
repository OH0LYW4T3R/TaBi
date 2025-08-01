package com.example.tabi.treasurehunt.mytreasurehunt.repository;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.treasurehunt.mytreasurehunt.PostStatus;
import com.example.tabi.treasurehunt.mytreasurehunt.entity.MyTreasureHunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyTreasureHuntRepository extends JpaRepository<MyTreasureHunt, Long> {
    List<MyTreasureHunt> findByAppUserAndStatus(AppUser appUser, PostStatus status);
}
