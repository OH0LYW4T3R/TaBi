package com.example.tabi.treasurehunt.treasurehuntpost.controller;

import com.example.tabi.treasurehunt.mytreasurehuntplay.vo.PositionRequest;
import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import com.example.tabi.treasurehunt.treasurehuntpost.service.TreasureHuntPostService;
import com.example.tabi.treasurehunt.treasurehuntpost.vo.TreasureHuntPostDto;
import com.example.tabi.treasurehunt.treasurehuntpost.vo.TreasureHuntPostRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
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
@RequestMapping("/api/treasure-hunt-post")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "TreasureHuntPost", description = "보물찾기 포스팅 관련 API")
public class TreasureHuntPostController {
    private final TreasureHuntPostService treasureHuntPostService;

    @Operation(
        summary = "보물찾기 생성",
        description = """
            사용자가 보물찾기를 생성.<br>  
            반드시 multipart/form-data형태로 요청을 보낼것.<br>
            .jpg, .jpeg, .png, .gif, .webp를 제외한 형태는 저장하지 않음<br>
            MIME TYPE은 image/jpeg, image/png, image/gif, image/webp만 허용됨.
            """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                schema = @Schema(implementation = TreasureHuntPostRequest.class)
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "보물찾기 생성 성공",
                content = @Content(schema = @Schema(implementation = TreasureHuntPostDto.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "존재하지 않는 유저거나, 잘못된 이미지 형식, 확장자",
                content = @Content(schema = @Schema(implementation = String.class))
            )
        }
    )
    @PostMapping(value = "/creation", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createTreasureHuntPost(Authentication authentication, @ModelAttribute TreasureHuntPostRequest treasureHuntPostRequest) {
        TreasureHuntPostDto treasureHuntPostDto = treasureHuntPostService.createTreasureHuntPost(authentication, treasureHuntPostRequest);

        if (treasureHuntPostDto == null)
            return ResponseEntity.badRequest().body("App User Not Found or Wrong Image Request");

        return ResponseEntity.ok(treasureHuntPostDto);
    }

    @Operation(
        summary = "보물찾기 피드 조회",
        description = """
            자신의 포스팅은 제외<br> 
            공개(`pub = true`) 상태이며 잠기지 않고(`locked = false`) 찾아지지 않은(`termination = false`) 보물찾기 포스트를 최신순으로 10개씩 페이징하여 조회<br>
            페이지 번호는 0부터 시작하며 최신순으로 pages:0 -> (0~9), pages:1 -> (10~19) ...<br>
            아래로 당겨 새로고침 하는것은 pages값을 0으로 보낼것.<br>
            밑으로 당겨 새로고침 하는것은 현재 pages + 1을 해서 보낼것.
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(
                name = "pages",
                description = "조회할 페이지 번호 (0부터 시작)",
                example = "0"
            )
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공 or No more posts",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TreasureHuntPostDto.class))
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "App User Not Found",
                content = @Content(schema = @Schema(implementation = String.class))
            )
        }
    )
    @GetMapping(value = "/read-ten")
    public ResponseEntity<?> readTenTreasureHuntPost(Authentication authentication, @RequestParam(name = "pages", defaultValue = "0") int pages) {
        List<TreasureHuntPostDto> treasureHuntPostDtos = treasureHuntPostService.readTenTreasureHuntPosts(authentication, pages);

        if (treasureHuntPostDtos == null)
            return ResponseEntity.badRequest().body("App User Not Found");

        if (treasureHuntPostDtos.isEmpty())
            return ResponseEntity.ok("No more posts");

        return ResponseEntity.ok(treasureHuntPostDtos);
    }


    @Operation(
        summary = "보물찾기 게시글 단건 조회",
        description = "주어진 ID 값으로 특정 보물찾기 게시글을 조회.",
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(
                name = "id",
                description = "조회할 보물찾기 게시글 ID",
                example = "1",
                required = true
            )
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(schema = @Schema(implementation = TreasureHuntPostDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "게시글이 존재하지 않음"
            )
        }
    )
    @GetMapping(value = "/read/{id}")
    public ResponseEntity<?> readTreasureHuntPostById(@PathVariable Long id) {
        TreasureHuntPostDto treasureHuntPostDto = treasureHuntPostService.getTreasureHuntPostById(id);

        if (treasureHuntPostDto == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(treasureHuntPostDto);
    }

    @Operation(
        summary = "보물찾기 플레이 시작",
        description = """
                요청한 보물찾기 게시글의 플레이버튼을 눌러 보물찾기를 시작 (실행중인 보물찾기) <br>
                이미 실행 중이거나 종료된 경우, 혹은 작성자가 본인인 경우 실행이 제한  <br>
                저장된 게시글 상태(SAVED)일 경우 삭제 후 실행으로 전환 <br>
                """,
        responses = {
            @ApiResponse(responseCode = "200", description = "플레이 시작 처리 성공 (사용자가 없거나 존재하지 않는 포스트면 아무 반응 없음)"),
            @ApiResponse(
                    responseCode = "400",
                    description = """
                        잘못된 요청으로 플레이 시작 실패. <br>
                        가능한 실패 사유: <br>
                        - User Not Found : 인증된 사용자가 없음 <br>
                        - Treasure Hunt Post Not Found : 해당 게시글이 존재하지 않음 <br>
                        - The creator cannot run. : 작성자는 실행 불가 <br>
                        - This is a post that is already running. : 이미 실행 중 <br>
                        - This post has been terminated. : 종료된 게시글 <br>
                        """
            )
        }
    )
    @PostMapping("/play")
    public ResponseEntity<?> playTreasureHuntPost(Authentication authentication, @RequestBody PositionRequest positionRequest) {
        String result = treasureHuntPostService.playTreasureHuntPost(authentication, positionRequest);

        if (result.equals("success"))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().body(result);
    }
}
