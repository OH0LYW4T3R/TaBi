package com.example.tabi.reward.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.myinventory.entity.MyInventory;
import com.example.tabi.myinventory.repository.MyInventoryRepository;
import com.example.tabi.myprofile.entity.MyProfile;
import com.example.tabi.myprofile.repository.MyProfileRepository;
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
    private static final Integer MAX_EXP = 50;

    private final RewardRepository rewardRepository;
    private final MyInventoryRepository myInventoryRepository;
    private final MyProfileRepository myProfileRepository;

    @Override
    @Transactional
    public Reward createReward(boolean questType) {
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

    @Override
    @Transactional
    public void addReward(AppUser appUser, Reward reward) {
        MyInventory myInventory = appUser.getMyInventory();
        MyProfile myProfile = appUser.getMyProfile();

        myInventory.setCoins(myInventory.getCoins() + reward.getCoin());

        if (myProfile.getExperience() + reward.getExperience() >= MAX_EXP) {
            myProfile.setLevel(myProfile.getLevel() + 1);
            myProfile.setExperience((myProfile.getExperience() + reward.getExperience()) - MAX_EXP);
        } else {
            myProfile.setExperience(myProfile.getExperience() + reward.getExperience());
        }

        if (reward.isType()) {
            // 일반 보상 (퀘스트)
            myInventory.setNormalCreditCard(myInventory.getNormalCreditCard() + reward.getCreditCardCount());
        } else {
            // 특별 보상 (보물찾기)
            myInventory.setUniqueCreditCard(myInventory.getUniqueCreditCard() + reward.getCreditCardCount());
        }

        myInventoryRepository.save(myInventory);
        myProfileRepository.save(myProfile);
    }

    public static RewardDto rewardToRewardDto(Reward reward) {
        return new RewardDto(reward.getRewardId(), reward.getExperience(), reward.isType(), reward.getCreditCardCount(), reward.getCoin());
    }
}
