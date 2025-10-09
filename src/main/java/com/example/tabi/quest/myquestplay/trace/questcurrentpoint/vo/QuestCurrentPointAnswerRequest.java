package com.example.tabi.quest.myquestplay.trace.questcurrentpoint.vo;

import com.example.tabi.quest.actions.ActionType;
import com.example.tabi.quest.actions.photopuzzleaction.vo.PhotoKeywordRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class QuestCurrentPointAnswerRequest {
    private ActionType actionType;

    //Photo Puzzle Action
    @Schema(type = "string", format = "binary")
    private MultipartFile submissionImage;

    //Location Puzzle Action
    private Double latitude;
    private Double longitude;
    private Double altitude;

    //Input Puzzle Action
    private String submissionAnswerString;
}
