package com.example.tabi.quest.myquestplay.trace.questhintsave.controller;

import com.example.tabi.quest.myquestplay.trace.questhintsave.service.QuestHintSaveService;
import com.example.tabi.quest.myquestplay.trace.questhintsave.vo.HintContentsDto;
import com.example.tabi.quest.myquestplay.trace.questhintsave.vo.HintSetDto;
import com.example.tabi.quest.myquestplay.trace.questhintsave.vo.QuestHintSaveRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quest-hint-save")
@Tag(name = "QuestHintSave", description = "힌트 구매 관련 API")
public class QuestHintSaveController {
    private final QuestHintSaveService questHintSaveService;

    @Operation(
        summary = "힌트 구매",
        description = "QuestCurrentPoint 기준 힌트 구매. 1→2→3 순서 강제. 코인 부족 시 실패.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "힌트 구매 요청",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = QuestHintSaveRequest.class),
                examples = {
                    @ExampleObject(
                        name = "첫 힌트 구매",
                        value = """
                            {
                              "questCurrentPointId": 101,
                              "purchaseHintIndex": 1
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "두 번째 힌트 구매",
                        value = """
                            {
                              "questCurrentPointId": 101,
                              "purchaseHintIndex": 2
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "세 번째 힌트 구매",
                        value = """
                            {
                              "questCurrentPointId": 101,
                              "purchaseHintIndex": 3
                            }
                            """
                    )
                }
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "구매 성공",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = HintContentsDto.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "구매 실패(인덱스/상태/권한/코인/중복 등)",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = HintContentsDto.class))
            )
        }
    )
    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseHint(Authentication authentication, @RequestBody QuestHintSaveRequest questHintSaveRequest) {
        HintContentsDto hintContentsDto = questHintSaveService.purchaseHint(authentication, questHintSaveRequest);
        if (hintContentsDto == null || hintContentsDto.getErrorMessage() != null) {
            return ResponseEntity.badRequest().body(hintContentsDto);
        }
        return ResponseEntity.ok(hintContentsDto);
    }

    @Operation(
        summary = "내 힌트 세트 조회",
        description = "questHintSaveId 기준 보유 힌트 상태와 구매한 힌트 내용 조회.<br>구매하지 않은 힌트에 대해서는 null",
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(name = "questHintSaveId", description = "QuestHintSave ID", required = true, example = "555")
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = HintSetDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "대상 없음 (없는 questHintSaveId, 없는 Action, 없는 ActionType)",
                content = @Content
            )
        }
    )
    @GetMapping("read/{questHintSaveId}")
    public ResponseEntity<?> readMyHint(@PathVariable Long questHintSaveId) {
        HintSetDto hintSetDto = questHintSaveService.readMyHint(questHintSaveId);
        if (hintSetDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(hintSetDto);
    }
}
