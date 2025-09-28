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
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestPostDto {
    private Long questPostId;

    private String uploadUserName;
    private String uploadUserProfileUrl;

    private String questTitle;
    private String questDescription;

    // 예상 소요시간
    private Integer estimatedDay;
    private Integer estimatedHour;
    private Integer estimatedMinute;
    // 예상 소요시간

    private boolean locked;
    private boolean pub;

    private Long questId;

    private PostCounterDto postCounterDto;
    private RewardDto rewardDto;
    private QuestStartLocationDto questStartLocationDto;
    private List<QuestPostImageDto> questPostImageDtos;

    private LocalDateTime createdAt;

    public static QuestPostDto questPostToQuestPostDto(QuestPost questPost) {
        QuestPostDto questPostDto = new QuestPostDto();

        if (questPost.getQuestPostId() != null) questPostDto.setQuestPostId(questPost.getQuestPostId());
        if (questPost.getUploadUserName() != null) questPostDto.setUploadUserName(questPost.getUploadUserName());
        if (questPost.getUploadUserProfileUrl() != null) questPostDto.setUploadUserProfileUrl(questPost.getUploadUserProfileUrl());
        if (questPost.getQuestTitle() != null) questPostDto.setQuestTitle(questPost.getQuestTitle());
        if (questPost.getQuestDescription() != null) questPostDto.setQuestDescription(questPost.getQuestDescription());

        if (questPost.getEstimatedDay() != null) questPostDto.setEstimatedDay(questPost.getEstimatedDay());
        if (questPost.getEstimatedHour() != null) questPostDto.setEstimatedHour(questPost.getEstimatedHour());
        if (questPost.getEstimatedMinute() != null) questPostDto.setEstimatedMinute(questPost.getEstimatedMinute());

        questPostDto.setLocked(questPost.isLocked());
        questPostDto.setPub(questPost.isPub());

        if (questPost.getQuest() != null) questPostDto.setQuestId(questPost.getQuest().getQuestId());

        if (questPost.getCreatedAt() != null) questPostDto.setCreatedAt(questPost.getCreatedAt());

        if (questPost.getPostCounter() != null) {
            questPostDto.setPostCounterDto(PostCounterServiceJpaImpl.postCounterToPostCounterDto(questPost.getPostCounter()));
        }
        if (questPost.getReward() != null) {
            questPostDto.setRewardDto(RewardServiceJpaImpl.rewardToRewardDto(questPost.getReward()));
        }
        if (questPost.getQuestStartLocation() != null) {
            questPostDto.setQuestStartLocationDto(QuestStartLocationDto.questStartLocationToQuestStartLocationDto(questPost.getQuestStartLocation()));
        }
        if (questPost.getQuestPostImages() != null && !questPost.getQuestPostImages().isEmpty()) {
            questPostDto.setQuestPostImageDtos(questPost.getQuestPostImages().stream().map(QuestPostImageDto::questPostImageToQuestPostImageDto).collect(Collectors.toList()));
        }

        return questPostDto;
    }
}
