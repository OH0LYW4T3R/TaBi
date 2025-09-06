package com.example.tabi.treasurehunt.mytreasurehuntplay.controller;

import com.example.tabi.util.PlayStatus;
import com.example.tabi.treasurehunt.mytreasurehuntplay.service.MyTreasureHuntPlayService;
import com.example.tabi.treasurehunt.mytreasurehuntplay.vo.MyTreasureHuntPlayDto;
import com.example.tabi.treasurehunt.mytreasurehuntplay.vo.PositionRequest;
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
@RequestMapping("/api/my-treasure-hunt-play")
@RequiredArgsConstructor
@Tag(name = "MyTreasureHuntPlay", description = "보물찾기 플레이 관련 API")
public class MyTreasureHuntPlayController {
    private final MyTreasureHuntPlayService myTreasureHuntPlayService;

    @Operation(
        summary = "AVAILABLE 상태 보물찾기 조회",
        description = """
            로그인한 사용자의 AVAILABLE 상태 보물찾기 목록을 조회 (종료된 게시글은 제외)<br>
            Figma의 보물찾기 실행부분의 "실행될 퀘스트가 여러개일 경우" 페이지 리스트에 올라가는 정보
        """,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = MyTreasureHuntPlayDto.class))
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
    public ResponseEntity<?> getAvailableTreasureHuntPlays(Authentication authentication) {
        List<MyTreasureHuntPlayDto> result = myTreasureHuntPlayService.getSpecificStatusTreasureHuntPlays(authentication, PlayStatus.AVAILABLE);

        if (result == null) {
            return ResponseEntity.badRequest().body("AppUser Not Found");
        }

        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "PENDING 상태 보물찾기 조회",
        description = "로그인한 사용자의 PENDING 상태 보물찾기 목록을 조회 (종료된 게시글은 제외).",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = MyTreasureHuntPlayDto.class))
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
    public ResponseEntity<?> getPendingTreasureHuntPlays(Authentication authentication) {
        List<MyTreasureHuntPlayDto> result = myTreasureHuntPlayService.getSpecificStatusTreasureHuntPlays(authentication, PlayStatus.PENDING);

        if (result == null) {
            return ResponseEntity.badRequest().body("AppUser Not Found");
        }

        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "PLAYING 상태 보물찾기 조회",
        description = "로그인한 사용자의 PLAYING 상태 보물찾기 목록을 조회 (종료된 게시글은 제외).",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = MyTreasureHuntPlayDto.class))
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
    public ResponseEntity<?> getPlayingTreasureHuntPlays(Authentication authentication) {
        List<MyTreasureHuntPlayDto> result = myTreasureHuntPlayService.getSpecificStatusTreasureHuntPlays(authentication, PlayStatus.PLAYING);

        if (result == null) {
            return ResponseEntity.badRequest().body("AppUser Not Found");
        }

        return ResponseEntity.ok(result);
    }


    @Operation(
        summary = "CLEARED 상태 보물찾기 조회",
        description = "로그인한 사용자의 CLEARED 상태 보물찾기 목록을 조회",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = MyTreasureHuntPlayDto.class))
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
    public ResponseEntity<?> getClearedTreasureHuntPlays(Authentication authentication) {
        List<MyTreasureHuntPlayDto> result = myTreasureHuntPlayService.getSpecificStatusTreasureHuntPlays(authentication, PlayStatus.CLEARED);

        if (result == null) {
            return ResponseEntity.badRequest().body("AppUser Not Found");
        }

        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "보물찾기 상태 - AVAILABLE로 전환",
        description = "사용자의 현재 위치가 시작 위치 반경 1km 이내인 경우 AVAILABLE 상태로 보물찾기를 등록.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "등록 성공",
                content = @Content(schema = @Schema(implementation = MyTreasureHuntPlayDto.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "등록 실패",
                content = @Content(schema = @Schema(implementation = MyTreasureHuntPlayDto.class))
            )
        }
    )
    @PostMapping("/available")
    public ResponseEntity<?> changeToAvailable(Authentication authentication, @RequestBody PositionRequest positionRequest) {
        MyTreasureHuntPlayDto result = myTreasureHuntPlayService.changeToSpecificStatusTreasureHuntPlay(authentication, positionRequest, PlayStatus.AVAILABLE);

        if (result.getErrorMessage() != null)
            return ResponseEntity.badRequest().body(result);

        return ResponseEntity.ok(result);
    }


    @Operation(
        summary = "보물찾기 상태 - PENDING으로 전환",
        description = "현재 PLAYING 상태인 보물찾기를 실행중에 뒤로가기를 누르면 일시중지(PENDING) 상태로 전환.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "전환 성공",
                content = @Content(schema = @Schema(implementation = MyTreasureHuntPlayDto.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "전환 실패",
                content = @Content(schema = @Schema(implementation = MyTreasureHuntPlayDto.class))
            )
        }
    )
    @PostMapping("/pending")
    public ResponseEntity<?> changeToPending(Authentication authentication, @RequestBody PositionRequest positionRequest) {
        MyTreasureHuntPlayDto result = myTreasureHuntPlayService.changeToSpecificStatusTreasureHuntPlay(authentication, positionRequest, PlayStatus.PENDING);

        if (result.getErrorMessage() != null)
            return ResponseEntity.badRequest().body(result);

        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "보물찾기 상태 - PLAYING으로 전환",
        description = "현재 PENDING 또는 AVAILABLE 상태인 보물찾기를 다시 실행하면 PLAYING 상태로 전환.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "전환 성공",
                content = @Content(schema = @Schema(implementation = MyTreasureHuntPlayDto.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "전환 실패",
                content = @Content(schema = @Schema(implementation = MyTreasureHuntPlayDto.class))
            )
        }
    )
    @PostMapping("/playing")
    public ResponseEntity<?> changeToPlaying(Authentication authentication, @RequestBody PositionRequest positionRequest) {
        MyTreasureHuntPlayDto result = myTreasureHuntPlayService.changeToSpecificStatusTreasureHuntPlay(authentication, positionRequest, PlayStatus.PLAYING);

        if (result.getErrorMessage() != null)
            return ResponseEntity.badRequest().body(result);

        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "보물찾기 상태 - CLEARED로 전환",
        description = "현재 위치가 시작 지점 반경 1.5m 이내인 경우 보물찾기를 완료 상태로 전환",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "전환 성공",
                content = @Content(schema = @Schema(implementation = MyTreasureHuntPlayDto.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "전환 실패",
                content = @Content(schema = @Schema(implementation = MyTreasureHuntPlayDto.class))
            )
        }
    )
    @PostMapping("/cleared")
    public ResponseEntity<?> changeToCleared(Authentication authentication, @RequestBody PositionRequest positionRequest) {
        MyTreasureHuntPlayDto result = myTreasureHuntPlayService.changeToSpecificStatusTreasureHuntPlay(authentication, positionRequest, PlayStatus.CLEARED);

        if (result.getErrorMessage() != null)
            return ResponseEntity.badRequest().body(result);

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "'Available' 상태의 보물찾기 플레이 삭제",
            description = "사용자가 저장한 보물찾기 중 'AVAILABLE'(진행 가능) 상태인 항목을 ID를 통해 삭제<br>1km 내에 벗어났다면 해당 api를 통해 벗어난 TreasureHuntPlay 객체 Id를 포함시켜 보내 삭제 요청",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "삭제 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class, example = "deletion available")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "삭제 실패",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class, example = "None id or not in Available state.")
                            )
                    )
            }
    )
    @DeleteMapping("/deletion/available/{id}")
    public ResponseEntity<?> deletionAvailableTreasureHuntPlay(Authentication authentication, @PathVariable Long id) {
        Boolean check = myTreasureHuntPlayService.deleteAvailableMyTreasureHuntPlay(authentication, id);

        if (check)
            return ResponseEntity.ok("deletion available");

        return ResponseEntity.badRequest().body("None id or not in Available state.");
    }
}
