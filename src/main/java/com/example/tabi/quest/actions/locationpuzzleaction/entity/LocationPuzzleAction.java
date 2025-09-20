package com.example.tabi.quest.actions.locationpuzzleaction.entity;

import com.example.tabi.quest.actions.action.entity.Action;
import com.example.tabi.quest.actions.hint.entity.Hint;
import com.example.tabi.quest.actions.hint.vo.HintDto;
import com.example.tabi.quest.actions.inputpuzzleaction.vo.InputPuzzleActionDto;
import com.example.tabi.quest.actions.locationpuzzleaction.vo.LocationPuzzleActionDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("LocationPuzzleAction")
public class LocationPuzzleAction extends Action {
    private String locationName;

    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "hint_id")
    private Hint hint;

    private String actualLocation;
    private Double latitude;
    private Double longitude;
    private Double altitude;

    @Override
    public LocationPuzzleActionDto actionToActionDto() {
        return new LocationPuzzleActionDto(getActionId(), getCharacterImageUrl(), getQuestStep().getQuestStepId(), getLocationName(), HintDto.hintToHintDto(hint), getActualLocation(), getLatitude(), getLongitude(), getAltitude());
    }
}
