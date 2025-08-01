package com.example.tabi.reward.service;

import com.example.tabi.reward.RewardConfig;
import com.example.tabi.reward.entity.Reward;
import com.example.tabi.reward.repository.RewardRepository;
import com.example.tabi.reward.vo.RewardDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class RewardServiceJpaImpl implements RewardService {
    private final RewardRepository rewardRepository;

    @Override
    @Transactional
    public Reward createReword(boolean questType) {
        Reward reward = new Reward();

        if (questType) {
            reward.setExperience(RewardConfig.QUEST_EXPERIENCE);
            reward.setType(RewardConfig.QUEST_TYPE);
            reward.setCreditCardCount(RewardConfig.QUEST_CREDIT_CARD_COUNT);
            reward.setCoin(RewardConfig.QUEST_COIN);
        } else {
            reward.setExperience(RewardConfig.TREASURE_HUNT_EXPERIENCE);
            reward.setType(RewardConfig.TREASURE_HUNT_TYPE);
            reward.setCreditCardCount(RewardConfig.TREASURE_HUNT_CREDIT_CARD_COUNT);
            reward.setCoin(RewardConfig.TREASURE_HUNT_COIN);
        }

        Reward save = rewardRepository.save(reward);
        rewardRepository.flush();

        System.out.println("Saved Reward: " + save.getRewardId().toString());

        return reward;
    }
    public static RewardDto rewardToRewardDto(Reward reward) {
        return new RewardDto(reward.getRewardId(), reward.getExperience(), reward.isType(), reward.getCreditCardCount(), reward.getCoin());
    }
}
