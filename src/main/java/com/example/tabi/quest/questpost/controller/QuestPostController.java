package com.example.tabi.quest.questpost.controller;

import com.example.tabi.quest.questpost.service.QuestPostService;
import com.example.tabi.quest.questpost.vo.FinalSettingQuestPostRequest;
import com.example.tabi.quest.questpost.vo.FullQuestPostDto;
import com.example.tabi.quest.questpost.vo.QuestPostDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quest-post")
@RequiredArgsConstructor
@Tag(name = "QuestPost", description = "퀘스트 포스팅 관련 API")
@Slf4j
public class QuestPostController {

    private final QuestPostService questPostService;

    // 1) 초기 설정 생성
    @Operation(
        summary = "퀘스트 포스트 초기 설정 생성",
        description = """
            퀘스트 만들기를 선택하고 다음 버튼에 연결해야할 API <br>
            퀘스트 포스트 초기화가 필요하므로 반드시 호출
            """,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "생성 성공",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = QuestPostDto.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "생성 실패(인증/계정 없음 등)",
                content = @Content(schema = @Schema(implementation = String.class))
            )
        }
    )
    @PostMapping("/creation/initial-setting")
    public ResponseEntity<?> initialSettingQuestPost(Authentication authentication) {
        QuestPostDto dto = questPostService.initialSettingQuestPost(authentication);
        if (dto == null) {
            return ResponseEntity.badRequest().body("Failed to initialize QuestPost");
        }
        return ResponseEntity.ok(dto);
    }

    // 2) 최종 설정 적용
    @Operation(
        summary = "퀘스트 포스트 최종 설정 적용",
        description = """
            퀘스트 포스트 최종 작성시 호출해야하는 API<br>
            Request에는 estimated~를 제외한 전부를 포함시켜 보내야함. (estimatedTime는 사용자가 셋팅하면 보내고 없으면 비워서 보내도 됨.)<br>
            """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "최종 설정 요청 객체",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = FinalSettingQuestPostRequest.class),
                examples = @ExampleObject(
                    name = "예시",
                    value = """
                        {
                          "questPostId": 123,
                          "questTitle": "숨은 보물 탐험",
                          "questDescription": "도심 속 비밀 장소를 찾아라",
                          "estimatedDay": "1",
                          "estimatedHour": "3",
                          "estimatedMinute": "20",
                          "pub": true
                        }
                        """
                )
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "설정 성공",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = FullQuestPostDto.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "설정 실패(Quest Post Id 없음 등) 혹은 이미 작업이 완료된 Quest Post",
                content = @Content(schema = @Schema(implementation = String.class))
            )
        }
    )
    @PutMapping("/creation/final-setting")
    public ResponseEntity<?> finalSettingQuestPost(@RequestBody FinalSettingQuestPostRequest finalSettingQuestPostRequest) {
        FullQuestPostDto dto = questPostService.finalSettingQuestPost(finalSettingQuestPostRequest);

        if (dto == null) {
            return ResponseEntity.badRequest().body("Failed to finalize QuestPost (See Swagger Description-400)");
        }
        return ResponseEntity.ok(dto);
    }

    // 3) 공개 포스트 10개 페이지 조회
    @Operation(
        summary = "퀘스트 포스트 목록 조회(10개 페이지)",
        description = """
            10개를 최신순으로 보냄.<br>
            맨 아래로 갈시 현재 Page+1을 해서 보낼것.
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(name = "pages", description = "페이지 인덱스(0부터 시작)", required = true, example = "0")
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = QuestPostDto.class))
                )
            ),
            @ApiResponse(responseCode = "204", description = "대상 없음")
        }
    )
    @GetMapping("/list/{pages}")
    public ResponseEntity<?> readTenQuestPosts(Authentication authentication, @PathVariable int pages) {
        List<QuestPostDto> list = questPostService.readTenQuestPosts(authentication, pages);
        if (list == null || list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }
}
