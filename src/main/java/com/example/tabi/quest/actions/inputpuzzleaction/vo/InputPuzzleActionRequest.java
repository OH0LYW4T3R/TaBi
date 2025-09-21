package com.example.tabi.quest.actions.inputpuzzleaction.vo;

import com.example.tabi.quest.queststep.entity.QuestStep;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputPuzzleActionRequest {
    private String answerString;
    private String characterImageUrl;
    private QuestStep questStep;
}
