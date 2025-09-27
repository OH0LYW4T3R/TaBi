package com.example.tabi.quest.questrunninglocation.controller;

import com.example.tabi.quest.questrunninglocation.service.QuestRunningLocationService;
import com.example.tabi.quest.questrunninglocation.vo.QuestRunningLocationDto;
import com.example.tabi.quest.questrunninglocation.vo.QuestRunningLocationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quest-running-location")
@RequiredArgsConstructor
@Tag(name = "QuestRunningLocation", description = "퀘스트 실행 위치 API")
public class QuestRunningLocationController {
    private final QuestRunningLocationService questRunningLocationService;

    @Operation(
        summary = "퀘스트 실행 위치 저장/갱신(전체 교체)",
        description = """
            지정한 questId에 대해 실행 위치 리스트를 전체 교체 방식으로 저장.<br>
            - 실행 위치 리스트를 보낼때, 반드시 순서(sequence)를 정렬 해서 보낼 것. (1, 2, 3, 4, - 현재 로직은 주는 그대로 저장하게 해둠.)<br>
            - 생성시, 갱신시는 반드시 Request에 누락된 부분 필드 없이 실행 위치 리스트 전부를 보낼 것.<br>
            - 단, 갱신시 이전에 생성한 QuestRunningLocation이 있을 수도 있고, 사용자가 새로 추가해서 생성되지 않은 QuestRunningLocation이 있을 수 있다.<br>
            - 이전에 생성한 QuestRunningLocation이 있으면, 무조건 questRunningLocationId 필드를 포함시키고, 이전에 생성한 QuestRunningLocation이 없다면 questRunningLocationId를 포함시키지 말 것.<br>
            - 처음 생성의 경우 questRunningLocationId를 넣지 말것. (애초에 알수 없음 생성전이라)
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(
                name = "questId",
                description = "진행 위치를 저장/갱신할 퀘스트 ID(존재해야 함)",
                required = true,
                example = "7"
            )
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "저장/갱신에 사용할 요청 리스트(QuestRunningLocationRequest[])",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = QuestRunningLocationRequest.class),
                examples = @ExampleObject(
                    name = "예시",
                    value = """
                        [
                          {
                            "sequence": 1,
                            "locationName": "한밭대학교",
                            "detailLocation": "대전광역시 유성구...",
                            "latitude": 36.351,
                            "longitude": 127.300,
                            "altitude": 85.2
                          },
                          {
                            "sequence": 2,
                            "locationName": "유성온천역",
                            "detailLocation": "대전 유성구 온천북로...",
                            "latitude": 36.362,
                            "longitude": 127.344,
                            "altitude": 92.0
                          }
                        ]
                        """
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "저장/갱신 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = QuestRunningLocationDto.class)))),
            @ApiResponse(responseCode = "400", description = "저장/갱신 실패(요청값 오류/부모(quest Id) 미존재 등)", content = @Content(schema = @Schema(implementation = String.class)))
        }
    )
    @PostMapping("/save/{questId}")
    public ResponseEntity<?> saveQuestRunningLocation(@PathVariable Long questId, @RequestBody List<QuestRunningLocationRequest> questRunningLocationRequests) {
        List<QuestRunningLocationDto> questRunningLocationDtos = questRunningLocationService.saveQuestRunningLocation(questId, questRunningLocationRequests);

        if (questRunningLocationDtos == null) {
            return ResponseEntity.badRequest().body("Not Exist Quest Id or Quest Running Location Request is Empty");
        }

        return ResponseEntity.ok(questRunningLocationDtos);
    }

    @Operation(
        summary = "퀘스트 실행 위치 전체 조회(리스트)",
        description = """
            지정한 questId의 모든 실행 위치를 반환.
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(
                name = "questId",
                description = "조회할 퀘스트 ID",
                required = true,
                example = "7"
            )
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = QuestRunningLocationDto.class)))),
            @ApiResponse(responseCode = "400", description = "부모(quest Id) 미존재")
        }
    )
    @GetMapping("/{questId}")
    public ResponseEntity<?> getQuestRunningLocations(@PathVariable Long questId) {
        List<QuestRunningLocationDto> questRunningLocationDtos = questRunningLocationService.getQuestRunningLocations(questId);
        if (questRunningLocationDtos == null) {
            return ResponseEntity.badRequest().body("Not Exist Quest Id");
        }

        return ResponseEntity.ok(questRunningLocationDtos);
    }

    @Operation(
        summary = "퀘스트 실행 위치 단건 삭제",
        description = """
            questRunningLocationId에 해당하는 퀘스트 실행 위치를 삭제.<br>
            - 실행 위치 삭제 버튼과 연결.
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(
                name = "questRunningLocationId",
                description = "삭제할 진행 위치 ID",
                required = true,
                example = "15"
            )
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "삭제 성공/삭제하지 못해도 200")
        }
    )
    @DeleteMapping("/{questRunningLocationId}")
    public ResponseEntity<?> deleteQuestRunningLocation(@PathVariable Long questRunningLocationId) {
        questRunningLocationService.deleteQuestRunningLocation(questRunningLocationId);
        return ResponseEntity.ok().build();
    }
}
