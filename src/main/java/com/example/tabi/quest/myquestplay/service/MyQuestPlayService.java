package com.example.tabi.quest.myquestplay.service;

import com.example.tabi.quest.myquestplay.vo.MyQuestPlayDto;
import com.example.tabi.quest.myquestplay.vo.PositionRequest;
import com.example.tabi.util.PlayStatus;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface MyQuestPlayService {
    MyQuestPlayDto changeToSpecificStatusQuestPlay(Authentication authentication, PositionRequest positionRequest, PlayStatus playStatus);
    List<MyQuestPlayDto> getSpecificStatusQuestPlays(Authentication authentication, PlayStatus playStatus);
}
