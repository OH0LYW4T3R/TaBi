package com.example.tabi.quest.myquest.controller;

import com.example.tabi.quest.myquest.service.MyQuestService;
import com.example.tabi.quest.questpost.vo.QuestPostDto;
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
@RequestMapping("/api/my-quest")
@RequiredArgsConstructor
@Tag(name = "MyQuest", description = "내 퀘스트 관련 API")
public class MyQuestController {
    private final MyQuestService myQuestService;

    @Operation(
        summary = "실행 계획 중인 퀘스트 조회",
        description = """
            현재 로그인된 사용자의 실행 계획 중인 퀘스트를 조회<br>
            실행 버튼(POST /api/quest-post/play)을 누르면 저장되어 조회하는 API 이며, 해당 조회를 통해 단말 로컬 저장소에 경로/위치 등의 정보를 저장해 두고, 사용자 현재 위치와 지속적으로 비교하는 용도로 사용.<br>
            해당 엔드포인트는 마이페이지 퀘스트 탭 화면의 '실행중인 퀘스트' 바인딩 용도이기도 함.<br>
            반경 15m 내에 진입 시 DB 관리를 위해 반드시 /available 로 요청을 전송.
            """,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = QuestPostDto.class))
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
    public ResponseEntity<?> getRunningQuestPosts(Authentication authentication) {
        List<QuestPostDto> posts = myQuestService.getRunningStatusQuestPosts(authentication);

        if (posts == null) {
            return ResponseEntity.badRequest().body("AppUser Not Found");
        }
        return ResponseEntity.ok(posts);
    }

    @Operation(
        summary = "퀘스트 저장(SAVED)",
        description = """
            questPostId에 해당하는 퀘스트를 내 퀘스트의 저장(SAVED) 상태로 전환<br>
            이미 참여 레코드가 있는 경우(제작자/플레이/이미 저장/종료) 또는 AppUser/게시글 미존재 시 null 반환을 컨트롤러에서 400으로 처리.
            """,
        parameters = {
            @Parameter(name = "questPostId", description = "QuestPost ID", required = true, example = "10")
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "저장 성공",
                content = @Content(schema = @Schema(implementation = QuestPostDto.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid Request (AppUser/게시글 미존재 또는 상태 전환 불가)",
                content = @Content(schema = @Schema(implementation = String.class))
            )
        }
    )
    @PostMapping("/save")
    public ResponseEntity<?> saveMyQuest(Authentication authentication, @RequestParam Long questPostId) {
        QuestPostDto dto = myQuestService.saveMyQuest(authentication, questPostId);
        if (dto == null) {
            return ResponseEntity.badRequest().body("Invalid Request");
        }
        return ResponseEntity.ok(dto);
    }

    @Operation(
        summary = "제작(CREATED) 상태 퀘스트 조회",
        description = "현재 로그인된 사용자의 CREATED 상태 퀘스트 목록을 조회. (UI의 프로필 -> 제작한 퀘스트 해당)",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = QuestPostDto.class))
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
    public ResponseEntity<?> getCreatedQuestPosts(Authentication authentication) {
        List<QuestPostDto> posts = myQuestService.getCreatedStatusQuestPosts(authentication);
        if (posts == null) {
            return ResponseEntity.badRequest().body("AppUser Not Found");
        }
        return ResponseEntity.ok(posts);
    }

    @Operation(
        summary = "저장(SAVED) 상태 퀘스트 조회",
        description = "현재 로그인된 사용자의 SAVED 상태 퀘스트 목록을 조회. (UI의 프로필 -> 저장한 퀘스트 해당)",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = QuestPostDto.class))
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
    public ResponseEntity<?> getSavedQuestPosts(Authentication authentication) {
        List<QuestPostDto> posts = myQuestService.getSavedStatusQuestPosts(authentication);
        if (posts == null) {
            return ResponseEntity.badRequest().body("AppUser Not Found");
        }
        return ResponseEntity.ok(posts);
    }

    @Operation(
        summary = "종료된 상태 퀘스트 조회",
        description = "현재 로그인된 사용자의 클리어된 퀘스트 목록을 조회. (UI의 프로필 -> 종료된 퀘스트 해당)",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = QuestPostDto.class))
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
    public ResponseEntity<?> getTerminatedQuestPosts(Authentication authentication) {
        List<QuestPostDto> posts = myQuestService.getTerminatedQuestPosts(authentication);
        if (posts == null) {
            return ResponseEntity.badRequest().body("AppUser Not Found");
        }
        return ResponseEntity.ok(posts);
    }

    @Operation(
        summary = "상대방의 실행 계획 중인 퀘스트 조회",
        description = "상대방 프로필 ID 기준으로 해당 사용자의 실행 계획 중(RUNNING) 퀘스트 목록 조회",
        parameters = {
            @Parameter(name = "myProfileId", description = "상대방 MyProfile ID", required = true, example = "42")
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = QuestPostDto.class))
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
    public ResponseEntity<?> getRunningQuestPostsForCounterparty(@RequestParam Long myProfileId) {
        List<QuestPostDto> posts = myQuestService.getRunningStatusQuestPostsForCounterparty(myProfileId);
        if (posts == null) {
            return ResponseEntity.badRequest().body("MyProfile Not Found");
        }
        return ResponseEntity.ok(posts);
    }

    @Operation(
        summary = "상대방의 제작(CREATED) 상태 퀘스트 조회",
        description = "상대방 프로필 ID 기준으로 해당 사용자의 CREATED 상태 퀘스트 목록 조회",
        parameters = {
            @Parameter(name = "myProfileId", description = "상대방 MyProfile ID", required = true, example = "42")
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = QuestPostDto.class))
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
    public ResponseEntity<?> getCreatedQuestPostsForCounterparty(@RequestParam Long myProfileId) {
        List<QuestPostDto> posts = myQuestService.getCreatedStatusQuestPostsForCounterparty(myProfileId);
        if (posts == null) {
            return ResponseEntity.badRequest().body("MyProfile Not Found");
        }
        return ResponseEntity.ok(posts);
    }

    @Operation(
        summary = "상대방의 저장(SAVED) 상태 퀘스트 조회",
        description = "상대방 프로필 ID 기준으로 해당 사용자의 SAVED 상태 퀘스트 목록 조회",
        parameters = {
            @Parameter(name = "myProfileId", description = "상대방 MyProfile ID", required = true, example = "42")
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = QuestPostDto.class))
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
    public ResponseEntity<?> getSavedQuestPostsForCounterparty(@RequestParam Long myProfileId) {
        List<QuestPostDto> posts = myQuestService.getSavedStatusQuestPostsForCounterparty(myProfileId);
        if (posts == null) {
            return ResponseEntity.badRequest().body("MyProfile Not Found");
        }
        return ResponseEntity.ok(posts);
    }

    @Operation(
        summary = "상대방의 종료(TERMINATED) 상태 퀘스트 조회",
        description = "상대방 프로필 ID 기준으로 해당 사용자의 TERMINATED 상태 퀘스트 목록 조회",
        parameters = {
            @Parameter(name = "myProfileId", description = "상대방 MyProfile ID", required = true, example = "42")
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = QuestPostDto.class))
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
    public ResponseEntity<?> getTerminatedQuestPostsForCounterparty(@RequestParam Long myProfileId) {
        List<QuestPostDto> posts = myQuestService.getTerminatedStatusQuestPostsForCounterparty(myProfileId);
        if (posts == null) {
            return ResponseEntity.badRequest().body("MyProfile Not Found");
        }
        return ResponseEntity.ok(posts);
    }
}
