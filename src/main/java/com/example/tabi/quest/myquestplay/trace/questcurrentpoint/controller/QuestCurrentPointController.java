package com.example.tabi.quest.myquestplay.trace.questcurrentpoint.controller;

import com.example.tabi.quest.myquestplay.trace.questcurrentpoint.service.QuestCurrentPointService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quest-current-point")
@RequiredArgsConstructor
@Tag(name = "QuestCurrentPoint", description = "실제 퀘스트 플레이 관련 API")
public class QuestCurrentPointController {
    private final QuestCurrentPointService questCurrentPointService;
}
