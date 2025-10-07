package com.example.tabi.quest.myquestplay.trace.questcurrentpoint.vo;

import com.example.tabi.quest.actions.talkingaction.vo.TalkingActionDto;
import lombok.Data;

@Data
public class QuestMostRecentTalkingActionDto {
    TalkingActionDto talkingActionDto;
    String errorMessage;
}
