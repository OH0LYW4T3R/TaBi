package com.example.tabi.quest.questpostcomment.controller;

import com.example.tabi.quest.questpostcomment.service.QuestPostCommentService;
import com.example.tabi.quest.questpostcomment.vo.QuestPostCommentDto;
import com.example.tabi.quest.questpostcomment.vo.QuestPostCommentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quest-post-comment")
@RequiredArgsConstructor
@Tag(name="QuestPostComment", description = "퀘스트 포스트 댓글 관련 API")
public class QuestPostCommentController {
    private final QuestPostCommentService questPostCommentService;

    @Operation(
        summary = "댓글 생성",
        description = """
            퀘스트 게시글에 부모/자식 댓글을 생성. <br>
            questPostId, comment 필드는 반드시 채울 것. <br>
            단, 대댓글인 경우 parentQuestPostCommentId(부모의 댓글 ID)를 함께 보낼 것.
            """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "댓글 생성 요청 바디",
            required = true,
            content = @Content(
                schema = @Schema(implementation = QuestPostCommentRequest.class),
                examples = {
                    @ExampleObject(
                        name = "부모 댓글 생성",
                        value = """
                            {
                              "questPostId": 101,
                              "comment": "좋은 퀘스트네요!"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "대댓글 생성",
                        value = """
                            {
                              "questPostId": 101,
                              "parentQuestPostCommentId": 555,
                              "comment": "저도 그렇게 생각해요!"
                            }
                            """
                    )
                }
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "댓글 생성 성공",
                content = @Content(schema = @Schema(implementation = QuestPostCommentDto.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = """
                    잘못된 요청으로 댓글 생성 실패.
                    가능한 실패 사유:
                    - AppUser Not Found : 인증된 사용자가 없음
                    - QuestPost Not Found : 게시글이 존재하지 않음
                    - QuestPostComment Not Found : 부모 댓글이 존재하지 않음
                    - Parent comments and posts are different : 부모 댓글과 게시글 불일치
                    - Reply does not allow comments : 대댓글에 또 댓글 불가
                    - 그 외 사유는 `errorMessage`에 포함
                    """,
                content = @Content(schema = @Schema(implementation = QuestPostCommentDto.class))
            )
        }
    )
    @PostMapping("/creation")
    public ResponseEntity<?> createQuestPostComment(Authentication authentication, @RequestBody QuestPostCommentRequest questPostCommentRequest) {
        QuestPostCommentDto questPostCommentDto = questPostCommentService.createQuestPostComment(authentication, questPostCommentRequest);
        if (questPostCommentDto.getErrorMessage() == null) return ResponseEntity.ok(questPostCommentDto);
        return ResponseEntity.badRequest().body(questPostCommentDto);
    }

    @Operation(
        summary = "댓글 삭제",
        description = """
            지정한 댓글을 삭제<br>
            작성자만 삭제 가능
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(name = "commentId", description = "삭제할 댓글 ID", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "댓글 삭제 성공 <br> 성공 요청은 보내지만 삭제되지 않을 수 있음. (응답 메시지 확인 요망)",
                content = @Content(schema = @Schema(implementation = String.class))
            )
        }
    )
    @DeleteMapping("/deletion/{commentId}")
    public ResponseEntity<?> deleteQuestPostComment(Authentication authentication, @PathVariable Long commentId) {
        return ResponseEntity.ok(questPostCommentService.deleteQuestPostComment(authentication, commentId));
    }

    @Operation(
        summary = "최상위 댓글 페이지 조회",
        description = """
            부모(최상위) 댓글을 페이지 단위로 조회
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(name = "page", description = "조회할 페이지(0-base)", required = true)
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "댓글 조회 조건(게시글 ID 등) - questPostId를 반드시 채울 것.",
            required = true,
            content = @Content(
                schema = @Schema(implementation = QuestPostCommentRequest.class),
                examples = {
                    @ExampleObject(
                        name = "부모 댓글 조회 예",
                        value = """
                            {
                              "questPostId": 101
                            }
                            """
                    )
                }
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "부모 댓글 목록 조회 성공",
                content = @Content(schema = @Schema(implementation = QuestPostCommentDto[].class))
            ),
            @ApiResponse(responseCode = "404", description = "앱유저 존재하지 않음 또는 퀘스트 포스트 존재하지 않음")
        }
    )
    @PostMapping("/parent/comment/{page}")
    public ResponseEntity<?> getParentQuestPostComment(Authentication authentication, @RequestBody QuestPostCommentRequest questPostCommentRequest, @PathVariable int page) {
        List<QuestPostCommentDto> questPostCommentDtos = questPostCommentService.getQuestPostComments(authentication, questPostCommentRequest, page);
        if (questPostCommentDtos == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(questPostCommentDtos);
    }

    @Operation(
        summary = "특정 부모의 자식 댓글 페이지 조회",
        description = """
            자식 댓글을 페이지 단위로 조회
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(name = "page", description = "조회할 페이지(0-base)", required = true)
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "댓글 조회 조건(게시글 ID 등) - questPostId, parentQuestPostCommentId를 반드시 채울 것.",
            required = true,
            content = @Content(
                schema = @Schema(implementation = QuestPostCommentRequest.class),
                examples = {
                    @ExampleObject(
                        name = "자식 댓글 조회 예",
                        value = """
                            {
                              "questPostId": 101,
                              "parentQuestPostCommentId": 555
                            }
                            """
                    )
                }
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "자식 댓글 목록 조회 성공",
                content = @Content(schema = @Schema(implementation = QuestPostCommentDto[].class))
            ),
            @ApiResponse(responseCode = "404", description = "앱유저, 퀘스트 포스트, 부모 댓글 중 하나 이상 존재하지 않음")
        }
    )
    @PostMapping("/child/comment/{page}")
    public ResponseEntity<?> getChildQuestPostComment(Authentication authentication, @RequestBody QuestPostCommentRequest questPostCommentRequest, @PathVariable int page) {
        List<QuestPostCommentDto> questPostCommentDtos = questPostCommentService.getQuestPostChildrenComments(authentication, questPostCommentRequest, page);
        if (questPostCommentDtos == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(questPostCommentDtos);
    }
}
