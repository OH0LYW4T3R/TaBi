package com.example.tabi.quest.queststep.controller;

import com.example.tabi.quest.queststep.service.QuestStepService;
import com.example.tabi.quest.queststep.vo.QuestStepDto;
import com.example.tabi.quest.queststep.vo.QuestStepRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quest-step")
@RequiredArgsConstructor
@Tag(name = "QuestStep", description = "퀘스트 Action 관련 API")
public class QuestStepController {
    private final QuestStepService questStepService;

    @Operation(
        summary = "퀘스트 액션 생성",
        description = """
            퀘스트 스팟 리스트중 한 스팟에 대하여 여러 액션 생성<br>
            한 스팟에 대해 액션 생성시 해당 API로 요청.
            """
            ,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = """
                    생성에 사용할 요청 객체(QuestStepRequest)<br>
                    ActionType을 꼭 채울것 - ActionType의 종류 -> WALKING, TALKING, STAYING, PHOTO_PUZZLE, LOCATION_PUZZLE, INPUT_PUZZLE (완전히 같게 할 것)<br>
                    sequence(액션의 순서 값)을 꼭 채울 것.<br>
                    characterImageUrl를 꼭 채울것<br><br>
                    
                    타입별 필수 필드 (ActionType이 결정되면 밑에 필수 필드를 꼭 채워야함. 다른 필드는 채우지 않아도 됨.)<br>
                    WALKING : walkingCount<br>
                    TALKING : story<br>
                    STAYING : day, hour, minute<br>
                    PHOTO_PUZZLE : photoKeywordRequests<br>
                    LOCATION_PUZZLE : locationName(보여지는 주소), actualLocation(실주소), latitude, longitude, altitude<br>
                    INPUT_PUZZLE : answerString<br><br>
                    
                    Puzzle Action인 경우는 hintOne, hintTwo, hintThree를 꼭 채울 것. (사용자가 채우지 않는 경우 null로 보낼것)
                    """,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = QuestStepRequest.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "생성 성공", content = @Content(schema = @Schema(implementation = QuestStepDto.class))),
            @ApiResponse(responseCode = "400", description = "생성 실패(요청값 오류/부모 미존재 등)", content = @Content(schema = @Schema(implementation = String.class)))
        }
    )
    @PostMapping("/creation")
    public ResponseEntity<?> createQuestStep(@RequestBody QuestStepRequest questStepRequest) {
        QuestStepDto dto = questStepService.createQuestStep(questStepRequest);

        if (dto == null)
            return ResponseEntity.badRequest().body("Failed to create QuestStep");

        return ResponseEntity.ok(dto);
    }

    @Operation(
        summary = "퀘스트 액션 수정",
        description = """
            QuestStep ID로 액션을 찾아 수정.<br>
            액션의 순서가 바뀌는 경우 무조건 해당 API로 요청할것. (3 -> 1 이동한경우 해당 API 호출, 만약 3->1로 이동하면 원래 1, 2번에 있던건 2, 3번으로 변경되니 해당 요소에 대해서도 API 호출)
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(
                name = "questStepId",
                description = "수정할 QuestStep ID",
                required = true,
                example = "10"
            )
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "수정에 사용할 요청 객체(QuestStepRequest)<br> 수정시에도 생성과 같은 규칙 적용. (퀘스트 액션 생성 API 문서 필독)",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = QuestStepRequest.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = QuestStepDto.class))),
            @ApiResponse(responseCode = "400", description = "수정 실패(요청값 오류/대상 없음 등)", content = @Content(schema = @Schema(implementation = String.class)))
        }
    )
    @PutMapping("/{questStepId}")
    public ResponseEntity<?> updateQuestStep(@PathVariable Long questStepId, @RequestBody QuestStepRequest questStepRequest) {
        QuestStepDto dto = questStepService.updateQuestStep(questStepId, questStepRequest);

        if (dto == null)
            return ResponseEntity.badRequest().body("Failed to update QuestStep");

        return ResponseEntity.ok(dto);
    }

    @Operation(
        summary = "퀘스트 액션 조회",
        description = "QuestStep ID로 액션 정보를 조회.",
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(
                name = "questStepId",
                description = "조회할 QuestStep ID",
                required = true,
                example = "10"
            )
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = QuestStepDto.class))),
            @ApiResponse(responseCode = "404", description = "대상 없음")
        }
    )
    @GetMapping("/{questStepId}")
    public ResponseEntity<?> retrieveQuestStep(@PathVariable Long questStepId) {
        QuestStepDto dto = questStepService.retrieveQuestStep(questStepId);

        if (dto == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(dto);
    }

     @Operation(
        summary = "퀘스트 액션 삭제",
        description = "QuestStep ID로 액션을 삭제.",
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(
                name = "questStepId",
                description = "삭제할 QuestStep ID",
                required = true,
                example = "10"
            )
        },
        responses = {
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "대상 없음")
        }
    )
    @DeleteMapping("/{questStepId}")
    public ResponseEntity<?> deleteQuestStep(@PathVariable Long questStepId) {
        questStepService.deleteQuestStep(questStepId);

        return ResponseEntity.noContent().build();
    }
}
