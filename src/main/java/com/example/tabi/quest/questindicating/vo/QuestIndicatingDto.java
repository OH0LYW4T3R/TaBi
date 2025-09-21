package com.example.tabi.quest.questindicating.vo;

import com.example.tabi.quest.questindicating.entity.QuestIndicating;
import com.example.tabi.quest.questrunninglocation.entity.QuestRunningLocation;
import com.example.tabi.quest.queststep.entity.QuestStep;
import com.example.tabi.quest.queststep.vo.QuestStepDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class QuestIndicatingDto {
    private Long questIndicatingId;

    private Integer actionCount;

    boolean talkingAction;
    boolean stayingAction;
    boolean puzzleAction;
    boolean walkingAction;

    private List<QuestStepDto> questStepDtos; // 퀘스트의 순서가 담김

    private Long questRunningLocationId;

    public static QuestIndicatingDto questIndicatingToQuestIndicatingDto(QuestIndicating questIndicating) {
        QuestIndicatingDto questIndicatingDto = new QuestIndicatingDto();
        questIndicatingDto.setQuestIndicatingId(questIndicating.getQuestIndicatingId());
        questIndicatingDto.setActionCount(questIndicating.getActionCount());
        questIndicatingDto.setTalkingAction(questIndicating.isTalkingAction());
        questIndicatingDto.setStayingAction(questIndicating.isStayingAction());
        questIndicatingDto.setPuzzleAction(questIndicating.isPuzzleAction());
        questIndicatingDto.setWalkingAction(questIndicating.isWalkingAction());
        questIndicatingDto.setQuestStepDtos(questIndicating.getQuestSteps().stream().map(QuestStepDto::questStepToQuestStepDto).collect(Collectors.toList()));
        questIndicatingDto.setQuestRunningLocationId(questIndicating.getQuestRunningLocation().getQuestRunningLocationId());

        return questIndicatingDto;
    }
}
