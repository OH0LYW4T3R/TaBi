package com.example.tabi.quest.actions.talkingaction.service;

import com.example.tabi.quest.actions.talkingaction.entity.TalkingAction;
import com.example.tabi.quest.actions.talkingaction.vo.TalkingActionDto;
import com.example.tabi.quest.actions.talkingaction.vo.TalkingActionRequest;

public interface TalkingActionService {
    TalkingAction createTalkingAction(TalkingActionRequest talkingActionRequest);
    TalkingAction retrieveTalkingAction(Long talkingActionId);
    TalkingAction updateTalkingAction(Long talkingActionId, TalkingActionRequest talkingActionRequest);
    void deleteTalkingAction(Long talkingActionId);
}

