package com.example.tabi.quest.questpost.vo;

import com.example.tabi.postcounter.vo.PostCounterDto;
import com.example.tabi.quest.questpostimage.vo.QuestPostImageDto;
import com.example.tabi.quest.queststartlocation.vo.QuestStartLocationDto;
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

    private PostCounterDto postCounter;
    private RewardDto reward;
    private List<QuestStartLocationDto> questStartLocations;
    private QuestPostImageDto questPostImage;

    // Quest Dto는 어떻게 할것?

    private LocalDateTime createdAt;
}
