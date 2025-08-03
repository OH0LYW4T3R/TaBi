package com.example.tabi.treasurehunt.mytreasurehunt.controller;

import com.example.tabi.treasurehunt.mytreasurehunt.service.MyTreasureHuntService;
import com.example.tabi.treasurehunt.treasurehuntpost.vo.TreasureHuntPostDto;
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
}
