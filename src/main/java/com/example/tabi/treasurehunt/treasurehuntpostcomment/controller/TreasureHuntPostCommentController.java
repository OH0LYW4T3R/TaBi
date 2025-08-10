package com.example.tabi.treasurehunt.treasurehuntpostcomment.controller;

import com.example.tabi.treasurehunt.mytreasurehuntplay.vo.PositionRequest;
import com.example.tabi.treasurehunt.treasurehuntpostcomment.service.TreasureHuntPostCommentService;
import com.example.tabi.treasurehunt.treasurehuntpostcomment.vo.TreasureHuntPostCommentDto;
import com.example.tabi.treasurehunt.treasurehuntpostcomment.vo.TreasureHuntPostCommentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/treasure-hunt-post-comment")
@RequiredArgsConstructor
@Tag(name = "TreasureHuntPostComment", description = "보물찾기 댓글 관련 API")
public class TreasureHuntPostCommentController {
    private final TreasureHuntPostCommentService treasureHuntPostCommentService;

    @Operation(
        summary = "댓글 생성",
        description = """
            보물찾기 게시글에 부모/자식 댓글을 생성. <br>
            treasureHuntPostId, comment 필드는 반드시 채울것. <br>
            단 대댓글인 경우 parentTreasureHuntPostCommentId(부모의 댓글 아이디)를 채워서 보낼것.
            """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "댓글 생성 요청 바디",
            required = true,
            content = @Content(schema = @Schema(implementation = TreasureHuntPostCommentRequest.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "댓글 생성 성공",
                content = @Content(schema = @Schema(implementation = TreasureHuntPostCommentDto.class))),
            @ApiResponse(responseCode = "400", description = """
                잘못된 요청으로 댓글 생성 실패.
                가능한 실패 사유:
                - User Not Found : 인증된 사용자가 없음
                - Treasure Hunt Post Not Found : 게시글이 존재하지 않음
                - Parent Comment Not Found : 부모 댓글이 존재하지 않음
                - 권한 또는 유효성 오류 등 기타 사유는 `errorMessage`에 포함
                """,
                content = @Content(schema = @Schema(implementation = TreasureHuntPostCommentDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 필요")
        }
    )
    @PostMapping("/creation")
    public ResponseEntity<?> createTreasureHuntPostComment(Authentication authentication, @RequestBody TreasureHuntPostCommentRequest treasureHuntPostCommentRequest) {
        TreasureHuntPostCommentDto treasureHuntPostCommentDto = treasureHuntPostCommentService.createTreasureHuntPostComment(authentication, treasureHuntPostCommentRequest);

        if (treasureHuntPostCommentDto.getErrorMessage() == null)
            return ResponseEntity.ok(treasureHuntPostCommentDto);

        return ResponseEntity.badRequest().body(treasureHuntPostCommentDto);
    }

    @Operation(
        summary = "댓글 삭제",
        description = """
            지정한 댓글을 삭제<br>
            작성자만 삭제 가능
            """,
        parameters = {
            @Parameter(name = "commentId", description = "삭제할 댓글 ID", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공 <br> 성공 요청은 보내지만 삭제되지 않을 수 있음. (응답 메시지 확인 요망)",
                content = @Content(schema = @Schema(implementation = String.class)))
        }
    )
    @DeleteMapping("/deletion/{commentId}")
    public ResponseEntity<?> deleteTreasureHuntPostComment(Authentication authentication, @PathVariable Long commentId) {
        return ResponseEntity.ok(treasureHuntPostCommentService.deleteTreasureHuntPostComment(authentication, commentId));
    }

    @Operation(
        summary = "최상위 댓글 페이지 조회",
        description = """
            부모(최상위) 댓글을 페이지 단위로 조회
            """,
        parameters = {
            @Parameter(name = "page", description = "조회할 페이지(0-base)", required = true)
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "댓글 조회 조건(게시글 ID 등) - treasureHuntPostId를 반드시 채울 것.",
            required = true,
            content = @Content(schema = @Schema(implementation = TreasureHuntPostCommentRequest.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "부모 댓글 목록 조회 성공",
                content = @Content(schema = @Schema(implementation = TreasureHuntPostCommentDto[].class))),
            @ApiResponse(responseCode = "404", description = "앱유저 존재하지 않음, 보물찾기 포스팅 존재하지 않음")
        }
    )
    @PostMapping("/parent/comment/{page}")
    public ResponseEntity<?> getParentTreasureHuntPostComment(Authentication authentication, @RequestBody TreasureHuntPostCommentRequest treasureHuntPostCommentRequest, @PathVariable int page) {
        List<TreasureHuntPostCommentDto> treasureHuntPostCommentDtos = treasureHuntPostCommentService.getTreasureHuntPostComments(authentication, treasureHuntPostCommentRequest, page);

        if (treasureHuntPostCommentDtos == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(treasureHuntPostCommentDtos);
    }

    @Operation(
        summary = "특정 부모의 자식 댓글 페이지 조회",
        description = """
            자식 댓글을 페이지 단위로 조회
            """,
        parameters = {
            @Parameter(name = "page", description = "조회할 페이지(0-base)", required = true)
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "댓글 조회 조건(게시글 ID 등) - treasureHuntPostId, parentTreasureHuntPostCommentId를 반드시 채울 것.",
            required = true,
            content = @Content(schema = @Schema(implementation = TreasureHuntPostCommentRequest.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "부모 댓글 목록 조회 성공",
                content = @Content(schema = @Schema(implementation = TreasureHuntPostCommentDto[].class))),
            @ApiResponse(responseCode = "404", description = "앱유저 존재하지 않음, 보물찾기 포스팅 존재하지 않음, 부모 커맨트 존재하지 않음")
        }
    )
    @PostMapping("/child/comment/{page}")
    public ResponseEntity<?> getChildTreasureHuntPostComment(Authentication authentication, @RequestBody TreasureHuntPostCommentRequest treasureHuntPostCommentRequest, @PathVariable int page) {
        List<TreasureHuntPostCommentDto> treasureHuntPostCommentDtos = treasureHuntPostCommentService.getTreasureHuntPostChildrenComments(authentication, treasureHuntPostCommentRequest, page);

        if (treasureHuntPostCommentDtos == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(treasureHuntPostCommentDtos);
    }
}
