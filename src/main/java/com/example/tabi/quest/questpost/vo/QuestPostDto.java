package com.example.tabi.quest.questpost.vo;

import com.example.tabi.postcounter.service.PostCounterServiceJpaImpl;
import com.example.tabi.postcounter.vo.PostCounterDto;
import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.quest.questpostimage.vo.QuestPostImageDto;
import com.example.tabi.quest.queststartlocation.vo.QuestStartLocationDto;
import com.example.tabi.reward.service.RewardServiceJpaImpl;
import com.example.tabi.reward.vo.RewardDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestPostDto {
    private Long questPostId;

    private String uploadUserName;
    private String uploadUserProfileUrl;

    private String questTitle;
    private String questDescription;

    private LocalDateTime estimatedTime;

    private boolean locked;
    private boolean pub;

    private Long questId;

    private PostCounterDto postCounterDto;
    private RewardDto rewardDto;
    private QuestStartLocationDto questStartLocationDto;
    private QuestPostImageDto questPostImageDto;

    private LocalDateTime createdAt;

    public static QuestPostDto questPostToQuestPostDto(QuestPost questPost) {
        QuestPostDto questPostDto = new QuestPostDto();

        if (questPost.getQuestPostId() != null) questPostDto.setQuestPostId(questPost.getQuestPostId());
        if (questPost.getUploadUserName() != null) questPostDto.setUploadUserName(questPost.getUploadUserName());
        if (questPost.getUploadUserProfileUrl() != null) questPostDto.setUploadUserProfileUrl(questPost.getUploadUserProfileUrl());
        if (questPost.getQuestTitle() != null) questPostDto.setQuestTitle(questPost.getQuestTitle());
        if (questPost.getQuestDescription() != null) questPostDto.setQuestDescription(questPost.getQuestDescription());
        if (questPost.getEstimatedTime() != null) questPostDto.setEstimatedTime(questPost.getEstimatedTime());

        questPostDto.setLocked(questPost.isLocked());
        questPostDto.setPub(questPost.isPub());

        if (questPost.getQuest() != null) questPostDto.setQuestId(questPost.getQuest().getQuestId());

        if (questPost.getCreatedAt() != null) questPostDto.setCreatedAt(questPost.getCreatedAt());

        // --- 연관 Dto 매핑 (분기만 두고 내부 비움) ---
        if (questPost.getPostCounter() != null) {
            questPostDto.setPostCounterDto(PostCounterServiceJpaImpl.postCounterToPostCounterDto(questPost.getPostCounter()));
        }
        if (questPost.getReward() != null) {
            questPostDto.setRewardDto(RewardServiceJpaImpl.rewardToRewardDto(questPost.getReward()));
        }
        if (questPost.getQuestStartLocation() != null) {
            // questPostDto.setQuestStartLocationDto(...);
        }
        if (questPost.getQuestPostImages() != null && !questPost.getQuestPostImages().isEmpty()) {
            // questPostDto.setQuestPostImageDto(...);
        }

        return questPostDto;
    }
}
