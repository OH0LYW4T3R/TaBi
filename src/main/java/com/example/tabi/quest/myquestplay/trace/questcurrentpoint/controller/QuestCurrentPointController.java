package com.example.tabi.quest.myquestplay.trace.questcurrentpoint.controller;

import com.example.tabi.quest.myquestplay.entity.MyQuestPlay;
import com.example.tabi.quest.myquestplay.repository.MyQuestPlayRepository;
import com.example.tabi.quest.myquestplay.trace.questcurrentpoint.entity.QuestCurrentPoint;
import com.example.tabi.quest.myquestplay.trace.questcurrentpoint.service.QuestCurrentPointService;
import com.example.tabi.quest.myquestplay.trace.questcurrentpoint.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/quest-current-point")
@RequiredArgsConstructor
@Tag(name = "QuestCurrentPoint", description = "실제 퀘스트 플레이 관련 API")
public class QuestCurrentPointController {
    private final QuestCurrentPointService questCurrentPointService;
    private final MyQuestPlayRepository myQuestPlayRepository;

    @Operation(
        summary = "현재 진행 액션 상세 조회",
        description = """
            myQuestPlayId 기준 현재 액션 상세 반환<br>
            해당 EndPoint로 계속 요청 보내면 저절로 다음 액션 보여줌<br>
            단 퍼즐액션의 경우 /answer-check/{myQuestPlayId}로 보내서 정답을 맞춰야지만 다음으로 넘어가짐.
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(name = "myQuestPlayId", description = "MyQuestPlay ID", required = true, example = "10")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = QuestCurrentPointDto.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패(없는 MyQuest ID/Play 상태가 아님)", content = @Content(schema = @Schema(implementation = String.class)))
        }
    )
    @GetMapping("/detail/{myQuestPlayId}")
    public ResponseEntity<?> retrieveDetail(@PathVariable Long myQuestPlayId) {
        QuestCurrentPointDto questCurrentPointDto = questCurrentPointService.retrieveQuestCurrentPointDetail(myQuestPlayId);
        if (questCurrentPointDto == null) return ResponseEntity.badRequest().body("Failed to retrieve current point detail");
        return ResponseEntity.ok(questCurrentPointDto);
    }

    @Operation(
        summary = "현재 위치 정보 조회",
        description = """
            myQuestPlayId 기준 현재 사용자가 실행 해야할 위치 정보 반환<br>
            추후 Available->Pending, Playing->Pending 상태로 전환되는 경우 해당 엔드포인트를 통해 로컬에 저장되어 있는 위치 정보를 응답데이터 위치 정보로 교체 할 것.
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(name = "myQuestPlayId", description = "MyQuestPlay ID", required = true, example = "10")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = QuestNextLocationDto.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패(대상/상태 오류 등)", content = @Content(schema = @Schema(implementation = QuestNextLocationDto.class)))
        }
    )
    @GetMapping("/current-location/info/{myQuestPlayId}")
    public ResponseEntity<?> currentLocation(@PathVariable Long myQuestPlayId) {
        QuestNextLocationDto questNextLocationDto = questCurrentPointService.retrieveQuestCurrentLocationInfo(myQuestPlayId);
        if (questNextLocationDto.getErrorMessage() != null) return ResponseEntity.badRequest().body(questNextLocationDto);
        return ResponseEntity.ok(questNextLocationDto);
    }

    @Operation(
        summary = "다음 위치 세팅",
        description = """
            실행해야할 위치를 다음 위치로 셋팅해주는 역할<br><br>
            
            요청시 조건<br>
            1. /detail/{myQuestPlayId}에서 전달받은 응답의 endAction 값이 True인 경우 해당 API로 요청 보낼것.<br>
            2. 다음 실행위치 반경 15m 안에 있어야 함. (다음 실행위치에 대한 미리보기는 /next-location/info/{myQuestPlayId}를 통해 알 수 있음)<br>
            3. 상태는 Playing 상태여야함.<br>
            성공 시 진행 포인트를 다음 위치/첫 액션으로 갱신
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(name = "myQuestPlayId", description = "MyQuestPlay ID", required = true, example = "10")
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "사용자 현재 좌표",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = QuestUserLocationRequest.class),
                examples = @ExampleObject(
                    name = "예시",
                    value = """
                        {
                          "latitude": 36.351,
                          "longitude": 127.300
                        }
                        """
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "세팅 성공",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = QuestNextLocationDto.class))),
            @ApiResponse(responseCode = "400", description = "세팅 실패(요청시 조건을 살펴볼 것.)", content = @Content(schema = @Schema(implementation = QuestNextLocationDto.class)))
        }
    )
    @PostMapping("/next-location/setting/{myQuestPlayId}")
    public ResponseEntity<?> nextLocation(@PathVariable Long myQuestPlayId, @RequestBody QuestUserLocationRequest request) {
        QuestNextLocationDto questNextLocationDto = questCurrentPointService.retrieveQuestNextLocationSetting(myQuestPlayId, request);
        if (questNextLocationDto.getErrorMessage() != null) return ResponseEntity.badRequest().body(questNextLocationDto);
        return ResponseEntity.ok(questNextLocationDto);
    }

    @Operation(
        summary = "다음 위치 정보 조회",
        description = """
            마지막 액션 완료 여부 검증<br><br>
            
            요청시 조건<br>
            1. 상태는 Playing 상태여야함.<br>
            2. /detail/{myQuestPlayId}에서 전달받은 응답의 endAction 값이 True인 경우 해당 API로 요청 보낼것.<br><br>
            
            사용자가 다음위치의 반경 15m에 들었다면 반드시 /next-location/setting/{myQuestPlayId}로 요청을 보낼 것.<br>
            만약 현재 퀘스트 실행위치가 마지막 실행 위치였다면 endLocation은 True로 반환됨.
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(name = "myQuestPlayId", description = "MyQuestPlay ID", required = true, example = "10")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = QuestNextLocationDto.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패(상태/진행오류 등)", content = @Content(schema = @Schema(implementation = QuestNextLocationDto.class)))
        }
    )
    @GetMapping("/next-location/info/{myQuestPlayId}")
    public ResponseEntity<?> nextLocationInfo(@PathVariable Long myQuestPlayId) {
        QuestNextLocationDto questNextLocationDto = questCurrentPointService.retrieveQuestNextLocationInfo(myQuestPlayId);
        if (questNextLocationDto.getErrorMessage() != null) return ResponseEntity.badRequest().body(questNextLocationDto);
        return ResponseEntity.ok(questNextLocationDto);
    }

    // 6) 퍼즐 정답 체크
    @Operation(
        summary = "퍼즐 정답 체크",
        description = """
            현재 액션이 퍼즐인지 검증<br>
            정답 일치 시 다음 액션으로 인덱스 이동<br>
            결과 반환
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(name = "myQuestPlayId", description = "MyQuestPlay ID", required = true, example = "10")
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "정답 체크 요청",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = QuestCurrentPointAnswerRequest.class),
                examples = @ExampleObject(
                    name = "INPUT_PUZZLE 예시",
                    value = """
                        {
                          "actionType": "INPUT_PUZZLE",
                          "submissionAnswerString": "HANBAT"
                        }
                        """
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "검증 성공",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = QuestAnswerCheckDto.class))),
            @ApiResponse(responseCode = "400", description = "검증 실패(타입/정답/상태 오류 등)", content = @Content(schema = @Schema(implementation = QuestAnswerCheckDto.class)))
        }
    )
    @PostMapping("/answer-check/{myQuestPlayId}")
    public ResponseEntity<?> checkAnswer(@PathVariable Long myQuestPlayId, @RequestBody QuestCurrentPointAnswerRequest request) {
        QuestAnswerCheckDto questAnswerCheckDto = questCurrentPointService.checkAnswer(myQuestPlayId, request);
        if (questAnswerCheckDto.getErrorMessage() != null || !questAnswerCheckDto.isAnswered()) {
            // 실패 사유 포함 응답
            return ResponseEntity.badRequest().body(questAnswerCheckDto);
        }
        return ResponseEntity.ok(questAnswerCheckDto);
    }

    @Operation(
        summary = "가장 최근 TALKING 액션 조회",
        description = """
            myQuestPlayId 기준 현재 액션부터 역순 검색<br>
            현재 위치로 부터 가장 최근인 TALKING 액션 반환<br>
            아직 디자인은 안되어 있지만, 재시작시 퍼즐부터 갑작스럽게 시작하는 경우에 최근 대화를 보여줄 수 있는 기능 만듦(혹시몰라서)<br>
            미발견 시 에러 메시지 포함 반환
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(name = "myQuestPlayId", description = "MyQuestPlay ID", required = true, example = "10")
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = QuestMostRecentTalkingActionDto.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "조회 실패(대상/상태/미발견 등)",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = QuestMostRecentTalkingActionDto.class))
            )
        }
    )
    @GetMapping("/most-recent-talking/{myQuestPlayId}")
    public ResponseEntity<?> retrieveMostRecentTalking(@PathVariable Long myQuestPlayId) {
        QuestMostRecentTalkingActionDto questMostRecentTalkingActionDto = questCurrentPointService.retrieveMostRecentTalkingAction(myQuestPlayId);
        if (questMostRecentTalkingActionDto == null || questMostRecentTalkingActionDto.getErrorMessage() != null) {
            return ResponseEntity.badRequest().body(questMostRecentTalkingActionDto);
        }
        return ResponseEntity.ok(questMostRecentTalkingActionDto);
    }

    // 7) 진행 포인트 삭제
    @Operation(
        summary = "진행 포인트 삭제",
        description = "questCurrentPointId 기준 삭제",
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(name = "questCurrentPointId", description = "QuestCurrentPoint ID", required = true, example = "55")
        },
        responses = {
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "400", description = "삭제 실패", content = @Content(schema = @Schema(implementation = String.class)))
        }
    )
    @DeleteMapping("/{questCurrentPointId}")
    public ResponseEntity<?> delete(@PathVariable Long questCurrentPointId) {
        questCurrentPointService.deleteQuestCurrentPointById(questCurrentPointId);
        return ResponseEntity.noContent().build();
    }
}
