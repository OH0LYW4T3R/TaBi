package com.example.tabi.reward.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.reward.entity.Reward;

public interface RewardService {
    // questType = True면 Quest
    // questType = False면 TreasureHunt
    Reward createReward(boolean questType);
    void addReward(AppUser appUser, Reward reward);
}
