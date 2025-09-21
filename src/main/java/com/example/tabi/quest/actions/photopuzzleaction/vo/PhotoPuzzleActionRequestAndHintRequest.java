package com.example.tabi.quest.actions.photopuzzleaction.vo;

import com.example.tabi.quest.actions.hint.vo.HintRequest;
import com.example.tabi.quest.actions.inputpuzzleaction.vo.InputPuzzleActionRequest;
import com.example.tabi.quest.actions.inputpuzzleaction.vo.InputPuzzleActionRequestAndHintRequest;
import com.example.tabi.quest.queststep.entity.QuestStep;
import com.example.tabi.quest.queststep.vo.QuestStepRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoPuzzleActionRequestAndHintRequest {
    private PhotoPuzzleActionRequest photoPuzzleActionRequest;
    private HintRequest hintRequest;

    public static PhotoPuzzleActionRequestAndHintRequest preparePhotoPuzzleActionRequestAndHintRequest(QuestStepRequest questStepRequest, QuestStep questStep) {
        PhotoPuzzleActionRequest photoPuzzleActionRequest = new PhotoPuzzleActionRequest();
        photoPuzzleActionRequest.setPhotoKeywordRequests(questStepRequest.getPhotoKeywordRequests());
        photoPuzzleActionRequest.setCharacterImageUrl(questStepRequest.getCharacterImageUrl());
        photoPuzzleActionRequest.setQuestStep(questStep);

        HintRequest hintRequest = new HintRequest();
        hintRequest.setHintOne(questStepRequest.getHintOne());
        hintRequest.setHintTwo(questStepRequest.getHintTwo());
        hintRequest.setHintThree(questStepRequest.getHintThree());

        return new PhotoPuzzleActionRequestAndHintRequest(photoPuzzleActionRequest, hintRequest);
    }
}
