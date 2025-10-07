package com.example.tabi.quest.myquestplay.trace.questhintsave.vo;

import com.example.tabi.quest.actions.hint.entity.Hint;
import com.example.tabi.quest.myquestplay.trace.questhintsave.entity.QuestHintSave;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HintSetDto {
    private Long questHintSaveId;
    private String hintOne;
    private String hintTwo;
    private String hintThree;

    public static HintSetDto createHintSetDto(QuestHintSave questHintSave, Hint hint) {
        HintSetDto hintSetDto = new HintSetDto();
        hintSetDto.setQuestHintSaveId(questHintSave.getQuestHintSaveId());

        if (!questHintSave.isHintOneLocked())
            hintSetDto.setHintOne(hint.getHintOne());
        else if (!questHintSave.isHintTwoLocked())
            hintSetDto.setHintTwo(hint.getHintTwo());
        else if (!questHintSave.isHintThreeLocked())
            hintSetDto.setHintThree(hint.getHintThree());

        return hintSetDto;
    }
}
