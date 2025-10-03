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
    private Integer sequence;
    private String actionType;
    private ActionDto actionDto;
    private Long questIndicatingId;

    public static QuestStepDto questStepToQuestStepDto(QuestStep questStep) {
        String extractActionType;
        Action action = questStep.getAction();

        if (action instanceof WalkingAction) {
            extractActionType = "WALKING";
        } else if (action instanceof TalkingAction) {
            extractActionType = "TALKING";
        } else if (action instanceof StayingAction) {
            extractActionType = "STAYING";
        } else if (action instanceof PhotoPuzzleAction) {
            extractActionType = "PHOTO_PUZZLE";
        } else if (action instanceof LocationPuzzleAction) {
            extractActionType = "LOCATION_PUZZLE";
        } else if (action instanceof InputPuzzleAction) {
            extractActionType = "INPUT_PUZZLE";
        } else {
            throw new IllegalArgumentException("Unsupported Action subtype: " + action.getClass());
        }

        return new QuestStepDto(
                questStep.getQuestStepId(),
                questStep.getSequence(),
                extractActionType,
                questStep.getAction().actionToActionDto(),
                questStep.getQuestIndicating().getQuestIndicatingId()
            );
    }
}
