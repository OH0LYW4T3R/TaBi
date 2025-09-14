package com.example.tabi.quest.actions.locationpuzzleaction.vo;

import com.example.tabi.quest.actions.ActionType;
import com.example.tabi.quest.actions.action.vo.ActionDto;
import com.example.tabi.quest.actions.hint.entity.Hint;
import com.example.tabi.quest.actions.hint.vo.HintDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationPuzzleActionDto extends ActionDto {
    private String locationName;

    private HintDto hintDto;

    private String actualLocation;
    private double latitude;
    private double longitude;
    private double altitude;

    public LocationPuzzleActionDto(Long actionId, String characterImageUrl, Long questStepId, String locationName, HintDto hintDto, String actualLocation, double latitude, double longitude, double altitude) {
        super(actionId, characterImageUrl, questStepId);
        this.locationName = locationName;
        this.hintDto = hintDto;
        this.actualLocation = actualLocation;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }
}
