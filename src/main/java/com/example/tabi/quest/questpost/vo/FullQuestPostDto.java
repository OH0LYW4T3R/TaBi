package com.example.tabi.quest.questpost.vo;

import com.example.tabi.postcounter.service.PostCounterServiceJpaImpl;
import com.example.tabi.postcounter.vo.PostCounterDto;
import com.example.tabi.quest.questindicating.entity.QuestIndicating;
import com.example.tabi.quest.questindicating.vo.QuestIndicatingDto;
import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.quest.questpostimage.vo.QuestPostImageDto;
import com.example.tabi.quest.questrunninglocation.entity.QuestRunningLocation;
import com.example.tabi.quest.questrunninglocation.vo.QuestRunningLocationDto;
import com.example.tabi.quest.queststartlocation.vo.QuestStartLocationDto;
import com.example.tabi.quest.queststep.entity.QuestStep;
import com.example.tabi.reward.service.RewardServiceJpaImpl;
import com.example.tabi.reward.vo.RewardDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class FullQuestPostDto {
    // quest Post
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
    // quest Post

    // quest 는 생략

    // quest Running Location
    private List<QuestRunningLocationDto> questRunningLocationDtos;

    // quest Indicating Location
    private List<QuestIndicatingDto> questIndicatingDtos;

    public static FullQuestPostDto questPostToFullQuestPostDto(QuestPost questPost) {
        FullQuestPostDto fullQuestPostDto = new FullQuestPostDto();

        if (questPost.getQuestPostId() != null) fullQuestPostDto.setQuestPostId(questPost.getQuestPostId());
        if (questPost.getUploadUserName() != null) fullQuestPostDto.setUploadUserName(questPost.getUploadUserName());
        if (questPost.getUploadUserProfileUrl() != null) fullQuestPostDto.setUploadUserProfileUrl(questPost.getUploadUserProfileUrl());
        if (questPost.getQuestTitle() != null) fullQuestPostDto.setQuestTitle(questPost.getQuestTitle());
        if (questPost.getQuestDescription() != null) fullQuestPostDto.setQuestDescription(questPost.getQuestDescription());

        if (questPost.getEstimatedDay() != null) fullQuestPostDto.setEstimatedDay(questPost.getEstimatedDay());
        if (questPost.getEstimatedHour() != null) fullQuestPostDto.setEstimatedHour(questPost.getEstimatedHour());
        if (questPost.getEstimatedMinute() != null) fullQuestPostDto.setEstimatedMinute(questPost.getEstimatedMinute());

        fullQuestPostDto.setLocked(questPost.isLocked());
        fullQuestPostDto.setPub(questPost.isPub());

        if (questPost.getQuest() != null) fullQuestPostDto.setQuestId(questPost.getQuest().getQuestId());

        if (questPost.getCreatedAt() != null) fullQuestPostDto.setCreatedAt(questPost.getCreatedAt());

        if (questPost.getPostCounter() != null) {
            fullQuestPostDto.setPostCounterDto(PostCounterServiceJpaImpl.postCounterToPostCounterDto(questPost.getPostCounter()));
        }
        if (questPost.getReward() != null) {
            fullQuestPostDto.setRewardDto(RewardServiceJpaImpl.rewardToRewardDto(questPost.getReward()));
        }
        if (questPost.getQuestStartLocation() != null) {
            fullQuestPostDto.setQuestStartLocationDto(QuestStartLocationDto.questStartLocationToQuestStartLocationDto(questPost.getQuestStartLocation()));
        }
        if (questPost.getQuestPostImages() != null && !questPost.getQuestPostImages().isEmpty()) {
            fullQuestPostDto.setQuestPostImageDtos(questPost.getQuestPostImages().stream().map(QuestPostImageDto::questPostImageToQuestPostImageDto).collect(Collectors.toList()));
        }

        if (Objects.requireNonNull(questPost.getQuest()).getQuestRunningLocations() != null && !questPost.getQuest().getQuestRunningLocations().isEmpty()) {
            List<QuestRunningLocation> questRunningLocations = questPost.getQuest().getQuestRunningLocations();
            questRunningLocations.sort(Comparator.comparingInt(QuestRunningLocation::getSequence));
            fullQuestPostDto.setQuestRunningLocationDtos(questRunningLocations.stream().map(QuestRunningLocationDto::questRunningLocationToQuestRunningLocationDto).collect(Collectors.toList()));

            List<QuestIndicatingDto> questIndicatingDtos = new ArrayList<>();
            for (QuestRunningLocation questRunningLocation : questRunningLocations) {
                QuestIndicating questIndicating = questRunningLocation.getQuestIndicating();
                questIndicating.getQuestSteps().sort(Comparator.comparingInt(QuestStep::getSequence));
                questIndicatingDtos.add(QuestIndicatingDto.questIndicatingToQuestIndicatingDto(questIndicating));
            }

            fullQuestPostDto.setQuestIndicatingDtos(questIndicatingDtos);
        }

        return fullQuestPostDto;
    }
}
