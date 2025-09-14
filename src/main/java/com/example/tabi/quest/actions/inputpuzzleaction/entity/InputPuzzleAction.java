package com.example.tabi.quest.actions.inputpuzzleaction.entity;

import com.example.tabi.quest.actions.action.entity.Action;
import com.example.tabi.quest.actions.hint.entity.Hint;
import com.example.tabi.quest.actions.hint.vo.HintDto;
import com.example.tabi.quest.actions.inputpuzzleaction.vo.InputPuzzleActionDto;
import com.example.tabi.quest.actions.talkingaction.vo.TalkingActionDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("InputPuzzleAction")
public class InputPuzzleAction extends Action {
    private String answerString;

    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "hint_id")
    private Hint hint;

    @Override
    public InputPuzzleActionDto actionToActionDto() {
        return new InputPuzzleActionDto(getActionId(), getCharacterImageUrl(), getQuestStep().getQuestStepId(), getAnswerString(), HintDto.hintToHintDto(hint));
    }
}
