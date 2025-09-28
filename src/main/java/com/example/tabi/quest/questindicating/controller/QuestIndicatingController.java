package com.example.tabi.quest.questindicating.controller;

import com.example.tabi.quest.questindicating.service.QuestIndicatingService;
import com.example.tabi.quest.questindicating.vo.QuestIndicatingDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/quest-indicating")
@RequiredArgsConstructor
@Tag(name = "QuestIndicating", description = "퀘스트 설정 API")
public class QuestIndicatingController {
    private final QuestIndicatingService questIndicatingService;

    @Operation(
        summary = "퀘스트 인디케이팅(설정) 일괄 조회",
        description = """
            전달한 questId가 속한 모든 QuestIndicating을 리스트로 반환.<br>
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(
                name = "questId",
                description = "기준이 될 Quest ID",
                required = true,
                example = "21"
            )
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = QuestIndicatingDto.class))
                )
            ),
            @ApiResponse(responseCode = "404", description = "Quest Id 없음")
        }
    )
    @GetMapping("/list/{questId}")
    public ResponseEntity<?> retrieveQuestIndicatings(@PathVariable Long questId) {
        List<QuestIndicatingDto> questIndicatings = questIndicatingService.retrieveQuestIndicatings(questId);

        if (questIndicatings == null || questIndicatings.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(questIndicatings);
    }

    @Operation(
        summary = "퀘스트 인디케이팅(설정) 조회",
        description = "QuestIndicating ID로 설정 정보를 조회.",
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(
                name = "questIndicatingId",
                description = "조회할 QuestIndicating ID",
                required = true,
                example = "10"
            )
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = QuestIndicatingDto.class)
                )
            ),
            @ApiResponse(responseCode = "404", description = "Quest Running Location Id 없음")
        }
    )
    @GetMapping("/{questIndicatingId}")
    public ResponseEntity<?> retrieveQuestIndicating(@PathVariable Long questIndicatingId) {
        QuestIndicatingDto questIndicatingDto = questIndicatingService.retrieveQuestIndicating(questIndicatingId);

        if (questIndicatingDto == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(questIndicatingDto);
    }
}
