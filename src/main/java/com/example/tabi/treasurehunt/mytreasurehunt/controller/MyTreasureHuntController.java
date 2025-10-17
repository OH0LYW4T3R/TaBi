package com.example.tabi.treasurehunt.mytreasurehunt.controller;

import com.example.tabi.treasurehunt.mytreasurehunt.service.MyTreasureHuntService;
import com.example.tabi.treasurehunt.treasurehuntpost.vo.TreasureHuntPostDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/my-treasure-hunt")
@RequiredArgsConstructor
@Tag(name = "MyTreasureHunt", description = "내 보물찾기 관련 API")
public class MyTreasureHuntController {
    private final MyTreasureHuntService myTreasureHuntService;

    @Operation(
        summary = "실행 계획 중인 보물찾기 조회",
        description = """
            현재 로그인된 사용자의 실행 계획 중인 보물찾기 조회<br>
            실행 버튼을 누르면 저장되는 곳.<br>
            해당 보물찾기 조회는 진짜 게임시작에 필요한 정보이며, 미리 사용자 휴대폰에 위치를 로컬 저장소에 저장해놓은 뒤, 사용자 현재 위치와 계속 비교할것.<br>
            /reading/playing과는 다르며, 해당 엔드포인트는 자신의 프로필에 보물찾기 -> 실행중인 보물찾기 바인딩 용도이기도 함.<br>
            반경 1km 내에 들어오면 반드시 /available로 요청을 보낼 것. (DB 관리 위함), pending 상태일경우에는 /available로 요청을 보낼 필요 없음.
            """,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TreasureHuntPostDto.class))
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "AppUser Not Found",
                content = @Content(schema = @Schema(implementation = String.class))
            )
        }
    )
    @GetMapping("/my-running")
    public ResponseEntity<?> getRunningTreasureHuntPosts(Authentication authentication) {
        List<TreasureHuntPostDto> posts = myTreasureHuntService.getRunningStatusTreasureHuntPosts(authentication);

        if (posts == null) {
            return ResponseEntity.badRequest().body("AppUser Not Found");
        }

        return ResponseEntity.ok(posts);
    }

    @Operation(
        summary = "보물찾기 저장(SAVED)",
        description = """
            treasureHuntPostId에 해당하는 보물찾기 포스팅을 내 보물찾기의 저장(SAVED) 상태로 저장<br>
            이미 참여 레코드가 있는 경우(제작자/플레이/이미 저장/종료) 또는 AppUser/게시글 미존재 시 null 반환을 컨트롤러에서 400으로 처리.
            """,
        parameters = {
            @Parameter(name = "treasureHuntPostId", description = "TreasureHuntPost ID", required = true, example = "10")
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "저장 성공",
                content = @Content(schema = @Schema(implementation = TreasureHuntPostDto.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid Request (AppUser/게시글 미존재 또는 상태 전환 불가)",
                content = @Content(schema = @Schema(implementation = String.class))
            )
        }
    )
    @PostMapping("/save")
    public ResponseEntity<?> saveMyTreasureHunt(Authentication authentication, @RequestParam Long treasureHuntPostId) {
        TreasureHuntPostDto dto = myTreasureHuntService.saveMyTreasureHunt(authentication, treasureHuntPostId);
        if (dto == null) {
            return ResponseEntity.badRequest().body("Invalid Request");
        }
        return ResponseEntity.ok(dto);
    }

    @Operation(
        summary = "제작(CREATED) 상태 보물찾기 조회",
        description = "현재 로그인된 사용자의 CREATED 상태 보물찾기 목록을 조회. (UI의 프로필 -> 제작한 퀘스트의 해당)",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TreasureHuntPostDto.class))
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "AppUser Not Found",
                content = @Content(schema = @Schema(implementation = String.class))
            )
        }
    )
    @GetMapping("/my-created")
    public ResponseEntity<?> getCreatedTreasureHuntPosts(Authentication authentication) {
        List<TreasureHuntPostDto> posts = myTreasureHuntService.getCreatedStatusTreasureHuntPosts(authentication);
        if (posts == null) {
            return ResponseEntity.badRequest().body("AppUser Not Found");
        }
        return ResponseEntity.ok(posts);
    }

    @Operation(
        summary = "저장(SAVED) 상태 보물찾기 조회",
        description = "현재 로그인된 사용자의 SAVED 상태 보물찾기 목록을 조회. (UI의 프로필 -> 저장한 퀘스트의 해당)",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TreasureHuntPostDto.class))
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "AppUser Not Found",
                content = @Content(schema = @Schema(implementation = String.class))
            )
        }
    )
    @GetMapping("/my-saved")
    public ResponseEntity<?> getSavedTreasureHuntPosts(Authentication authentication) {
        List<TreasureHuntPostDto> posts = myTreasureHuntService.getSavedStatusTreasureHuntPosts(authentication);
        if (posts == null) {
            return ResponseEntity.badRequest().body("AppUser Not Found");
        }
        return ResponseEntity.ok(posts);
    }

    @Operation(
        summary = "종료(TERMINATED) 상태 보물찾기 조회",
        description = "현재 로그인된 사용자의 TERMINATED 상태 보물찾기 목록을 조회. (UI의 프로필 -> 종료된 퀘스트의 해당)",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TreasureHuntPostDto.class))
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "AppUser Not Found",
                content = @Content(schema = @Schema(implementation = String.class))
            )
        }
    )
    @GetMapping("/my-terminated")
    public ResponseEntity<?> getTerminatedTreasureHuntPosts(Authentication authentication) {
        List<TreasureHuntPostDto> posts = myTreasureHuntService.getTerminatedStatusTreasureHuntPosts(authentication);
        if (posts == null) {
            return ResponseEntity.badRequest().body("AppUser Not Found");
        }
        return ResponseEntity.ok(posts);
    }

    @Operation(
        summary = "상대방의 실행 계획 중인 보물찾기 조회",
        description = """
            상대방 프로필 ID 기준으로 해당 사용자의 실행 계획 중(RUNNING) 보물찾기 목록을 조회
            """,
        parameters = {
            @Parameter(name = "myProfileId", description = "상대방 MyProfile ID", required = true, example = "42")
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TreasureHuntPostDto.class))
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "MyProfile Not Found",
                content = @Content(schema = @Schema(implementation = String.class))
            )
        }
    )
    @GetMapping("/counterparty-running")
    public ResponseEntity<?> getRunningTreasureHuntPostsForCounterparty(@RequestParam Long myProfileId) {
        List<TreasureHuntPostDto> posts = myTreasureHuntService.getRunningStatusTreasureHuntPostsForCounterparty(myProfileId);
        if (posts == null) {
            return ResponseEntity.badRequest().body("MyProfile Not Found");
        }
        return ResponseEntity.ok(posts);
    }

    @Operation(
        summary = "상대방의 제작(CREATED) 상태 보물찾기 조회",
        description = "상대방 프로필 ID 기준으로 해당 사용자의 CREATED 상태 보물찾기 목록을 조회",
        parameters = {
            @Parameter(name = "myProfileId", description = "상대방 MyProfile ID", required = true, example = "42")
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TreasureHuntPostDto.class))
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "MyProfile Not Found",
                content = @Content(schema = @Schema(implementation = String.class))
            )
        }
    )
    @GetMapping("/counterparty-created")
    public ResponseEntity<?> getCreatedTreasureHuntPostsForCounterparty(@RequestParam Long myProfileId) {
        List<TreasureHuntPostDto> posts = myTreasureHuntService.getCreatedStatusTreasureHuntPostsForCounterparty(myProfileId);
        if (posts == null) {
            return ResponseEntity.badRequest().body("MyProfile Not Found");
        }
        return ResponseEntity.ok(posts);
    }

    @Operation(
        summary = "상대방의 저장(SAVED) 상태 보물찾기 조회",
        description = "상대방 프로필 ID 기준으로 해당 사용자의 SAVED 상태 보물찾기 목록을 조회",
        parameters = {
            @Parameter(name = "myProfileId", description = "상대방 MyProfile ID", required = true, example = "42")
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TreasureHuntPostDto.class))
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "MyProfile Not Found",
                content = @Content(schema = @Schema(implementation = String.class))
            )
        }
    )
    @GetMapping("/counterparty-saved")
    public ResponseEntity<?> getSavedTreasureHuntPostsForCounterparty(@RequestParam Long myProfileId) {
        List<TreasureHuntPostDto> posts = myTreasureHuntService.getSavedStatusTreasureHuntPostsForCounterparty(myProfileId);
        if (posts == null) {
            return ResponseEntity.badRequest().body("MyProfile Not Found");
        }
        return ResponseEntity.ok(posts);
    }

    @Operation(
        summary = "상대방의 종료(TERMINATED) 상태 보물찾기 조회",
        description = "상대방 프로필 ID 기준으로 해당 사용자의 TERMINATED 상태 보물찾기 목록을 조회",
        parameters = {
            @Parameter(name = "myProfileId", description = "상대방 MyProfile ID", required = true, example = "42")
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TreasureHuntPostDto.class))
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "MyProfile Not Found",
                content = @Content(schema = @Schema(implementation = String.class))
            )
        }
    )
    @GetMapping("/counterparty-terminated")
    public ResponseEntity<?> getTerminatedTreasureHuntPostsForCounterparty(@RequestParam Long myProfileId) {
        List<TreasureHuntPostDto> posts = myTreasureHuntService.getTerminatedStatusTreasureHuntPostsForCounterparty(myProfileId);
        if (posts == null) {
            return ResponseEntity.badRequest().body("MyProfile Not Found");
        }
        return ResponseEntity.ok(posts);
    }
}
