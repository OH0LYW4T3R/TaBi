package com.example.tabi.quest.queststep.vo;

import com.example.tabi.quest.actions.ActionType;
import com.example.tabi.quest.actions.photopuzzleaction.vo.PhotoKeywordRequest;
import com.example.tabi.quest.queststep.entity.QuestStep;
import lombok.Data;

import java.util.List;

@Data
public class QuestStepRequest {
    private Long questIndicatingId;

    private Integer sequence;

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

    //Photo Puzzle Action
    private List<PhotoKeywordRequest> photoKeywordRequests;

    //Location Puzzle Action
    private String locationName;
    private String actualLocation;
    private Double latitude;
    private Double longitude;
    private Double altitude;

    //Input Puzzle Action
    private String answerString;

    // If Puzzle Action
    private String hintOne;
    private String hintTwo;
    private String hintThree;
}
