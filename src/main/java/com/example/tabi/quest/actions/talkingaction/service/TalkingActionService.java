package com.example.tabi.quest.actions.talkingaction.service;

import com.example.tabi.quest.actions.talkingaction.vo.TalkingActionDto;
import com.example.tabi.quest.actions.talkingaction.vo.TalkingActionRequest;

public interface TalkingActionService {
    TalkingActionDto createTalkingAction(TalkingActionRequest talkingActionRequest);
    TalkingActionDto retrieveTalkingAction(Long talkingActionId);
    TalkingActionDto updateTalkingAction(Long talkingActionId, TalkingActionRequest talkingActionRequest);
    void deleteTalkingAction(Long talkingActionId);
}
