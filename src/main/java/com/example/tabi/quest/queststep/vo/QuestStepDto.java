package com.example.tabi.quest.queststep.vo;

import com.example.tabi.quest.actions.action.entity.Action;
import com.example.tabi.quest.actions.action.vo.ActionDto;
import com.example.tabi.quest.actions.inputpuzzleaction.entity.InputPuzzleAction;
import com.example.tabi.quest.actions.locationpuzzleaction.entity.LocationPuzzleAction;
import com.example.tabi.quest.actions.photopuzzleaction.entity.PhotoPuzzleAction;
import com.example.tabi.quest.actions.stayingaction.entity.StayingAction;
import com.example.tabi.quest.actions.talkingaction.entity.TalkingAction;
import com.example.tabi.quest.actions.walkingaction.entity.WalkingAction;
import com.example.tabi.quest.queststep.entity.QuestStep;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestStepDto {
    private Long questStepId;
    private ActionDto actionDto;
    private Long questIndicatingId;

    public static QuestStepDto questStepToQuestStepDto(QuestStep questStep) {
        return new QuestStepDto(
                questStep.getQuestStepId(),
                questStep.getAction().actionToActionDto(),
                questStep.getQuestIndicating().getQuestIndicatingId()
            );
    }
}
