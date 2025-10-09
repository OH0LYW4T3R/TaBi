package com.example.tabi.quest.myquestplay.controller;

import com.example.tabi.quest.myquestplay.service.MyQuestPlayService;
import com.example.tabi.quest.myquestplay.vo.MyQuestPlayDto;
import com.example.tabi.quest.myquestplay.vo.PositionRequest;
import com.example.tabi.util.PlayStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/my-quest-play")
@RequiredArgsConstructor
@Tag(name = "MyQuestPlay", description = "퀘스트 플레이 관련 API")
public class MyQuestPlayController {
    private final MyQuestPlayService myQuestPlayService;

    @Operation(
        summary = "AVAILABLE 상태 퀘스트 조회",
        description = """
            로그인한 사용자의 AVAILABLE 상태 퀘스트 목록을 조회합. (포스트가 공개, Non Block인 경우만 조회)
            """,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = MyQuestPlayDto.class))
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "AppUser Not Found",
                content = @Content(schema = @Schema(implementation = String.class))
            )
        }
    )
    @GetMapping("/reading/available")
    public ResponseEntity<?> getAvailableQuestPlays(Authentication authentication) {
        List<MyQuestPlayDto> result = myQuestPlayService.getSpecificStatusQuestPlays(authentication, PlayStatus.AVAILABLE);

        if (result == null) {
            return ResponseEntity.badRequest().body("AppUser Not Found");
        }
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "PENDING 상태 퀘스트 조회",
        description = """
            로그인한 사용자의 PENDING(일시중지) 상태 퀘스트 목록을 조회.
            """,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = MyQuestPlayDto.class))
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "AppUser Not Found",
                content = @Content(schema = @Schema(implementation = String.class))
            )
        }
    )
    @GetMapping("/reading/pending")
    public ResponseEntity<?> getPendingQuestPlays(Authentication authentication) {
        List<MyQuestPlayDto> result = myQuestPlayService.getSpecificStatusQuestPlays(authentication, PlayStatus.PENDING);

        if (result == null) {
            return ResponseEntity.badRequest().body("AppUser Not Found");
        }
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "PLAYING 상태 퀘스트 조회",
        description = """
            로그인한 사용자의 PLAYING(진행중) 상태 퀘스트 목록을 조회.
            """,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = MyQuestPlayDto.class))
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "AppUser Not Found",
                content = @Content(schema = @Schema(implementation = String.class))
            )
        }
    )
    @GetMapping("/reading/playing")
    public ResponseEntity<?> getPlayingQuestPlays(Authentication authentication) {
        List<MyQuestPlayDto> result = myQuestPlayService.getSpecificStatusQuestPlays(authentication, PlayStatus.PLAYING);

        if (result == null) {
            return ResponseEntity.badRequest().body("AppUser Not Found");
        }
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "CLEARED 상태 퀘스트 조회",
        description = """
            로그인한 사용자의 CLEARED(완료) 상태 퀘스트 목록을 조회. <br>
            endAction, endLocation이 모두 True인 상태면 해당 API로 요청하여 클리어 상태로 변경할 것.
            """,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = MyQuestPlayDto.class))
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "AppUser Not Found",
                content = @Content(schema = @Schema(implementation = String.class))
            )
        }
    )
    @GetMapping("/reading/cleared")
    public ResponseEntity<?> getClearedQuestPlays(Authentication authentication) {
        List<MyQuestPlayDto> result = myQuestPlayService.getSpecificStatusQuestPlays(authentication, PlayStatus.CLEARED);

        if (result == null) {
            return ResponseEntity.badRequest().body("AppUser Not Found");
        }
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "퀘스트 상태 - AVAILABLE로 전환",
        description = """
            현재 위치가 '퀘스트 시작 위치' 반경 15m(0.015km) 이내일 때 AVAILABLE 상태로 등록.<br>
            첫 등록 시 MyQuestPlay 레코드를 생성, PENDING에서 재접근 시 반경 확인 후 AVAILABLE로 복귀.<br>
            Available에서 갈 수 있는 상태 -> Playing(여러개의 실행 리스트 중 해당 퀘스트가 선택된 경우), Pending(실행 위치로 부터 5km 벗어난 경우)
            <br>swagger의 표시 오류로 Request Body에 treasureHuntPostId가 아니라 questPostId를 넣어줘야함.
            """,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "등록/전환 성공",
                content = @Content(schema = @Schema(implementation = MyQuestPlayDto.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "등록/전환 실패 (에러 메시지 본문 참조)",
                content = @Content(schema = @Schema(implementation = MyQuestPlayDto.class))
            )
        }
    )
    @PostMapping("/available")
    public ResponseEntity<?> changeToAvailable(Authentication authentication, @RequestBody PositionRequest positionRequest) {
        MyQuestPlayDto result = myQuestPlayService.changeToSpecificStatusQuestPlay(authentication, positionRequest, PlayStatus.AVAILABLE);

        if (result.getErrorMessage() != null)
            return ResponseEntity.badRequest().body(result);
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "퀘스트 상태 - PENDING으로 전환",
        description = """
            PLAYING 또는 AVAILABLE 상태인 퀘스트를 일시중지(PENDING)로 전환.<br>
            Pending의 기준은 실행위치 기준 5km 반경을 벗어난 경우 Pending 요청 할것.<br>
            서버는 현재 진행 지점을 SavePoint로 저장하여 이후 복귀 시 복원.<br>
            Pending에서 갈 수 있는 상태 -> Available(실행 위치로 부터 15m 반경안에 든 경우)
            <br>swagger의 표시 오류로 Request Body에 treasureHuntPostId가 아니라 questPostId를 넣어줘야함.
            """,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "전환 성공",
                content = @Content(schema = @Schema(implementation = MyQuestPlayDto.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "전환 실패 (에러 메시지 본문 참조)",
                content = @Content(schema = @Schema(implementation = MyQuestPlayDto.class))
            )
        }
    )
    @PostMapping("/pending")
    public ResponseEntity<?> changeToPending(Authentication authentication, @RequestBody PositionRequest positionRequest) {
        MyQuestPlayDto result = myQuestPlayService.changeToSpecificStatusQuestPlay(authentication, positionRequest, PlayStatus.PENDING);

        if (result.getErrorMessage() != null)
            return ResponseEntity.badRequest().body(result);
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "퀘스트 상태 - PLAYING으로 전환",
        description = """
            AVAILABLE 상태의 퀘스트를 다시 실행(PLAYING)으로 전환.<br>
            SavePoint가 있는 경우 SavePoint를 로드하여 이어서 진행. <br>
            Playing에서 갈 수 있는 상태 -> Pending(실행 위치로 부터 5km 벗어난 경우), Cleared(모든 퀘스트를 다 클리어한 경우)
            <br>swagger의 표시 오류로 Request Body에 treasureHuntPostId가 아니라 questPostId를 넣어줘야함.
            """,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "전환 성공",
                content = @Content(schema = @Schema(implementation = MyQuestPlayDto.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "전환 실패 (에러 메시지 본문 참조)",
                content = @Content(schema = @Schema(implementation = MyQuestPlayDto.class))
            )
        }
    )
    @PostMapping("/playing")
    public ResponseEntity<?> changeToPlaying(Authentication authentication, @RequestBody PositionRequest positionRequest) {
        MyQuestPlayDto result = myQuestPlayService.changeToSpecificStatusQuestPlay(authentication, positionRequest, PlayStatus.PLAYING);

        if (result.getErrorMessage() != null)
            return ResponseEntity.badRequest().body(result);
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "퀘스트 상태 - CLEARED로 전환",
        description = """
            현재 진행 지점이 마지막 지점(액션/러닝 로케이션 인덱스)이면 퀘스트를 완료(CLEARED)로 전환.<br>
            완료 시 리워드 지급 후 상태를 CLEARED로 저장.
            <br>swagger의 표시 오류로 Request Body에 treasureHuntPostId가 아니라 questPostId를 넣어줘야함.
            """,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "전환 성공",
                content = @Content(schema = @Schema(implementation = MyQuestPlayDto.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "전환 실패 (에러 메시지 본문 참조)",
                content = @Content(schema = @Schema(implementation = MyQuestPlayDto.class))
            )
        }
    )
    @PostMapping("/cleared")
    public ResponseEntity<?> changeToCleared(Authentication authentication, @RequestBody PositionRequest positionRequest) {
        MyQuestPlayDto result = myQuestPlayService.changeToSpecificStatusQuestPlay(authentication, positionRequest, PlayStatus.CLEARED);

        if (result.getErrorMessage() != null)
            return ResponseEntity.badRequest().body(result);
        return ResponseEntity.ok(result);
    }
}
