package com.example.tabi.reward.service;

import com.example.tabi.reward.entity.Reward;

public interface RewardService {
    // questType = True면 Quest
    // questType = False면 TreasureHunt
    Reward createReword(boolean questType);
}
