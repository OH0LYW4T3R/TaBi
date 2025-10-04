package com.example.tabi.quest.myquestplay.trace.questcurrentpoint.vo;

import com.example.tabi.quest.actions.ActionType;
import com.example.tabi.quest.actions.photopuzzleaction.vo.PhotoKeywordRequest;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class QuestCurrentPointAnswerRequest {
    private ActionType actionType;

    //Photo Puzzle Action
    private MultipartFile submissionImage;

    //Location Puzzle Action
    private Double latitude;
    private Double longitude;
    private Double altitude;

    //Input Puzzle Action
    private String submissionAnswerString;
}
