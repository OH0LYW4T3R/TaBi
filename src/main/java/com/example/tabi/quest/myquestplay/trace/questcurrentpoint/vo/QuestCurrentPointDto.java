package com.example.tabi.quest.myquestplay.trace.questcurrentpoint.vo;

import com.example.tabi.quest.actions.ActionType;
import com.example.tabi.quest.actions.action.entity.Action;
import com.example.tabi.quest.actions.inputpuzzleaction.entity.InputPuzzleAction;
import com.example.tabi.quest.actions.locationpuzzleaction.entity.LocationPuzzleAction;
import com.example.tabi.quest.actions.photopuzzleaction.entity.PhotoPuzzleAction;
import com.example.tabi.quest.actions.photopuzzleaction.vo.PhotoKeywordRequest;
import com.example.tabi.quest.actions.stayingaction.entity.StayingAction;
import com.example.tabi.quest.actions.talkingaction.entity.TalkingAction;
import com.example.tabi.quest.actions.walkingaction.entity.WalkingAction;
import com.example.tabi.quest.myquestplay.trace.questcurrentpoint.entity.QuestCurrentPoint;
import com.example.tabi.quest.myquestplay.trace.questhintsave.entity.QuestHintSave;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class QuestCurrentPointDto {
    private Long questCurrentPointId;

    private ActionType actionType; // WALKING, TALKING, STAYING, PHOTO_PUZZLE, LOCATION_PUZZLE, INPUT_PUZZLE

    //Common Field
    private String characterImageUrl;

    //Walking Action
    private Integer walkingCount;

    //Talking Action
    private String story;

    //Staying Action
    private Integer day;
    private Integer hour;
    private Integer minute;

    // If Puzzle Action
    private String hintOne;
    private String hintTwo;
    private String hintThree;

    private Boolean endAction;

    public static QuestCurrentPointDto questCurrentPointToQuestCurrentPointDto(QuestCurrentPoint questCurrentPoint, Action action, QuestHintSave questHintSave) {
        QuestCurrentPointDto questCurrentPointDto = new QuestCurrentPointDto();

        questCurrentPointDto.setQuestCurrentPointId(questCurrentPoint.getQuestCurrentPointId());
        questCurrentPointDto.setCharacterImageUrl(action.getCharacterImageUrl());

        System.out.println(questCurrentPoint.getCurrentActionIndex().toString() + questCurrentPoint.getEndActionIndex());
        if (Objects.equals(questCurrentPoint.getCurrentActionIndex(), questCurrentPoint.getEndActionIndex()))
            questCurrentPointDto.setEndAction(true);
        else questCurrentPointDto.setEndAction(false);

        if (action instanceof WalkingAction walkingAction) {
            questCurrentPointDto.setActionType(ActionType.WALKING);
            questCurrentPointDto.setWalkingCount(walkingAction.getWalkingCount());
        } else if (action instanceof TalkingAction talkingAction) {
            questCurrentPointDto.setActionType(ActionType.TALKING);
            questCurrentPointDto.setStory(talkingAction.getStory());
        } else if (action instanceof StayingAction stayingAction) {
            questCurrentPointDto.setActionType(ActionType.STAYING);
            questCurrentPointDto.setDay(stayingAction.getDay());
            questCurrentPointDto.setHour(stayingAction.getHour());
            questCurrentPointDto.setMinute(stayingAction.getMinute());
        } else if (action instanceof PhotoPuzzleAction photoPuzzleAction) {
            questCurrentPointDto.setActionType(ActionType.PHOTO_PUZZLE);

            if (questHintSave != null) {
                if (!questHintSave.isHintOneLocked())
                    questCurrentPointDto.setHintOne(photoPuzzleAction.getHint().getHintOne());
                if (!questHintSave.isHintTwoLocked())
                    questCurrentPointDto.setHintTwo(photoPuzzleAction.getHint().getHintTwo());
                if (!questHintSave.isHintThreeLocked())
                    questCurrentPointDto.setHintThree(photoPuzzleAction.getHint().getHintThree());
            }
        } else if (action instanceof LocationPuzzleAction locationPuzzleAction) {
            questCurrentPointDto.setActionType(ActionType.LOCATION_PUZZLE);

            if (questHintSave != null) {
                if (!questHintSave.isHintOneLocked())
                    questCurrentPointDto.setHintOne(locationPuzzleAction.getHint().getHintOne());
                if (!questHintSave.isHintTwoLocked())
                    questCurrentPointDto.setHintTwo(locationPuzzleAction.getHint().getHintTwo());
                if (!questHintSave.isHintThreeLocked())
                    questCurrentPointDto.setHintThree(locationPuzzleAction.getHint().getHintThree());
            }
        } else if (action instanceof InputPuzzleAction inputPuzzleAction) {
            questCurrentPointDto.setActionType(ActionType.INPUT_PUZZLE);

            if (questHintSave != null) {
                if (!questHintSave.isHintOneLocked())
                    questCurrentPointDto.setHintOne(inputPuzzleAction.getHint().getHintOne());
                if (!questHintSave.isHintTwoLocked())
                    questCurrentPointDto.setHintTwo(inputPuzzleAction.getHint().getHintTwo());
                if (!questHintSave.isHintThreeLocked())
                    questCurrentPointDto.setHintThree(inputPuzzleAction.getHint().getHintThree());
            }
        } else {
            throw new IllegalArgumentException("Unsupported Action subtype: " + action.getClass());
        }

        return questCurrentPointDto;
    }
}
