package com.example.tabi.quest.actions.locationpuzzleaction.vo;

import com.example.tabi.quest.actions.hint.vo.HintRequest;
import com.example.tabi.quest.actions.inputpuzzleaction.vo.InputPuzzleActionRequest;
import com.example.tabi.quest.actions.inputpuzzleaction.vo.InputPuzzleActionRequestAndHintRequest;
import com.example.tabi.quest.queststep.entity.QuestStep;
import com.example.tabi.quest.queststep.vo.QuestStepRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationPuzzleActionRequestAndHintRequest {
    private LocationPuzzleActionRequest locationPuzzleActionRequest;
    private HintRequest hintRequest;

    public static LocationPuzzleActionRequestAndHintRequest prepareLocationPuzzleActionRequestAndHintRequest(QuestStepRequest questStepRequest, QuestStep questStep) {
        LocationPuzzleActionRequest locationPuzzleActionRequest = new LocationPuzzleActionRequest();
        locationPuzzleActionRequest.setLocationName(questStepRequest.getLocationName());
        locationPuzzleActionRequest.setActualLocation(questStepRequest.getActualLocation());
        locationPuzzleActionRequest.setLatitude(questStepRequest.getLatitude());
        locationPuzzleActionRequest.setLongitude(questStepRequest.getLongitude());
        locationPuzzleActionRequest.setAltitude(questStepRequest.getAltitude());
        locationPuzzleActionRequest.setCharacterImageUrl(questStepRequest.getCharacterImageUrl());
        locationPuzzleActionRequest.setQuestStep(questStep);

        HintRequest hintRequest = new HintRequest();
        hintRequest.setHintOne(questStepRequest.getHintOne());
        hintRequest.setHintTwo(questStepRequest.getHintTwo());
        hintRequest.setHintThree(questStepRequest.getHintThree());

        return new LocationPuzzleActionRequestAndHintRequest(locationPuzzleActionRequest, hintRequest);
    }
}
