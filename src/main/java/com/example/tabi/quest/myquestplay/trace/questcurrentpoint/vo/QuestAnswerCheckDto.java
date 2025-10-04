package com.example.tabi.quest.myquestplay.trace.questcurrentpoint.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestAnswerCheckDto {
    private boolean answered;
    private String errorMessage;

    public static QuestAnswerCheckDto createQuestAnswerCheckDto(boolean answered, String errorMessage) {
        return new QuestAnswerCheckDto(
                answered,
                errorMessage
        );
    }
}
