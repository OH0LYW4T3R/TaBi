package com.example.tabi.quest.questpost.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quest-post")
@RequiredArgsConstructor
@Tag(name = "QuestPost", description = "퀘스트 포스팅 관련 API")
public class QuestPostController {
}
