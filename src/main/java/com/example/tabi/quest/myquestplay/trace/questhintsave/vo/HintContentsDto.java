package com.example.tabi.quest.myquestplay.trace.questhintsave.vo;

import com.example.tabi.quest.myquestplay.trace.questhintsave.entity.QuestHintSave;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HintContentsDto {
    private Long questHintSaveId;
    private String hintContents;
    private String errorMessage;

    public static HintContentsDto questHintSaveToHintContentsDto(QuestHintSave questHintSave, String hintContents) {
        return new HintContentsDto(
                questHintSave.getQuestHintSaveId(),
                hintContents,
                null
        );
    }
}
