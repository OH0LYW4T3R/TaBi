package com.example.tabi.quest.myquest.controller;

import com.example.tabi.quest.myquest.service.MyQuestService;
import com.example.tabi.quest.questpost.vo.QuestPostDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
