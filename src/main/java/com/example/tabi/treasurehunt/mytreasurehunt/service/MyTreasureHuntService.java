package com.example.tabi.treasurehunt.mytreasurehunt.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import org.springframework.security.core.Authentication;

public interface MyTreasureHuntService {
    void createMyTreasureHunt(AppUser appUser, TreasureHuntPost treasureHuntPost);
}
