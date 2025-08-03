package com.example.tabi.treasurehunt.mytreasurehuntplay.repository;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.treasurehunt.mytreasurehuntplay.PlayStatus;
import com.example.tabi.treasurehunt.mytreasurehuntplay.entity.MyTreasureHuntPlay;
import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyTreasureHuntPlayRepository extends JpaRepository<MyTreasureHuntPlay, Long> {
    MyTreasureHuntPlay findByAppUserAndTreasureHuntPost(AppUser appUser, TreasureHuntPost treasureHuntPost);
    List<MyTreasureHuntPlay> findByAppUserAndPlayStatus(AppUser appUser, PlayStatus playStatus);
}
