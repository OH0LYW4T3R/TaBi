package com.example.tabi.follow.controller;

import com.example.tabi.follow.service.FollowService;
import com.example.tabi.follow.vo.FollowDto;
import com.example.tabi.follow.vo.RetrieveProfileDto;
import com.example.tabi.follow.vo.RetrieveProfileRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
@Tag(name = "Follow", description = "SNS 관련 API")
public class FollowController {
    private final FollowService followService;

    @Operation(
        summary = "팔로우 요청/자동수락",
        description = "상대의 팔로우 정책에 따라 REQUESTED 또는 FOLLOWED로 생성.<br> 자동 수락이 디폴트며 매뉴얼 모드일 땐 Request로 날아감."
    )
    @Parameters({
        @Parameter(name = "followeeId", description = "팔로우 대상 AppUser ID", required = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = FollowDto.class)))
    @ApiResponse(responseCode = "400", description = "요청 불가(errorMessage 확인)", content = @Content(schema = @Schema(implementation = FollowDto.class)))
    @PostMapping("/follow-request/{followeeId}")
    public ResponseEntity<FollowDto> followRequest(Authentication authentication, @PathVariable Long followeeId) {
        FollowDto followDto = followService.followRequest(authentication, followeeId);
        return okOrBadRequest(followDto);
    }

    @Operation(
        summary = "팔로워 제거/언팔",
        description = "상대가 나를 FOLLOWED 중일 때 Unfollow"
    )
    @Parameters({
        @Parameter(name = "followeeId", description = "대상 AppUser ID", required = true)
    })
    @ApiResponse(responseCode = "200", description = "성공(문구 반환 or errorMessage)")
    @DeleteMapping("/unfollow/{followeeId}")
    public ResponseEntity<String> followCancel(Authentication authentication, @PathVariable Long followeeId) {
        String message = followService.followCancel(authentication, followeeId);
        return ResponseEntity.ok(message);
    }

    @Operation(
        summary = "내 팔로워 목록 조회",
        description = "나를 FOLLOWED 중인 사용자 목록을 반환."
    )
    @ApiResponse(
        responseCode = "200",
        description = "성공 (빈 배열 반환시 - 팔로워가 없거나, 로그인 사용자가 잘못되었거나)",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = RetrieveProfileDto.class)))
    )
    @GetMapping("/my-followers")
    public ResponseEntity<List<RetrieveProfileDto>> retrieveMyFollowers(Authentication authentication) {
        List<RetrieveProfileDto> followers = followService.retrieveMyFollowers(authentication);
        return ResponseEntity.ok(followers);
    }

    @Operation(
        summary = "내 팔로잉 목록 조회",
        description = "내가 FOLLOWED 중인(=내가 팔로우하는) 사용자 목록을 반환."
    )
    @ApiResponse(
        responseCode = "200",
        description = "성공 (빈 배열 반환시 - 내가 팔로잉 한 사람이 없거나, 로그인 사용자가 잘못되었거나)",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = RetrieveProfileDto.class)))
    )
    @GetMapping("/my-followings")
    public ResponseEntity<List<RetrieveProfileDto>> retrieveMyFollowings(Authentication authentication) {
        List<RetrieveProfileDto> followings = followService.retrieveMyFollowings(authentication);
        return ResponseEntity.ok(followings);
    }

    @Operation(
        summary = "차단",
        description = "요청/팔로우 상태를 정리하고 BLOCKED로 전환."
    )
    @Parameters({
        @Parameter(name = "followeeId", description = "차단 대상 AppUser ID", required = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = FollowDto.class)))
    @ApiResponse(responseCode = "400", description = "요청 불가(errorMessage 확인)", content = @Content(schema = @Schema(implementation = FollowDto.class)))
    @PostMapping("/block/{followeeId}")
    public ResponseEntity<FollowDto> blockRequest(Authentication authentication, @PathVariable Long followeeId) {
        FollowDto followDto = followService.blockRequest(authentication, followeeId);
        return okOrBadRequest(followDto);
    }

    @Operation(
        summary = "차단 해제",
        description = "내가 건 BLOCKED 삭제."
    )
    @Parameters({
        @Parameter(name = "followId", description = "BLOCKED Follow ID", required = true)
    })
    @ApiResponse(responseCode = "200", description = "성공(문구 반환 or errorMessage)")
    @DeleteMapping("/block/{followId}")
    public ResponseEntity<String> unBlockRequest(Authentication authentication, @PathVariable Long followId
    ) {
        String message = followService.unBlockRequest(authentication, followId);
        return ResponseEntity.ok(message);
    }

    @Operation(
        summary = "차단 목록 조회",
        description = "내가 차단한 사용자 목록을 반환."
    )
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = FollowDto.class))))
    @GetMapping("/blocked-list")
    public ResponseEntity<List<FollowDto>> retrieveBlockedUsers(Authentication authentication) {
        List<FollowDto> followDtoList = followService.retrieveBlockedUsers(authentication);
        return ResponseEntity.ok(followDtoList);
    }

    @Operation(
        summary = "팔로우 요청 수락",
        description = "자신이 메뉴얼 모드일 경우 나에게 들어온 REQUESTED를 수락하여 상호 FOLLOWED로 만듦."
    )
    @Parameters({
        @Parameter(name = "followId", description = "요청 Follow ID", required = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = FollowDto.class)))
    @ApiResponse(responseCode = "400", description = "요청 불가(errorMessage 확인)", content = @Content(schema = @Schema(implementation = FollowDto.class)))
    @PostMapping("/requests/{followId}/accept")
    public ResponseEntity<FollowDto> acceptFollowRequest(Authentication authentication, @PathVariable Long followId) {
        FollowDto followDto = followService.acceptFollowRequest(authentication, followId);
        return okOrBadRequest(followDto);
    }

    @Operation(
        summary = "팔로우 요청 거절",
        description = "나에게 들어온 REQUESTED를 삭제하고 DECLINED으로 변경."
    )
    @Parameters({
        @Parameter(name = "followId", description = "요청 Follow ID", required = true)
    })
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = FollowDto.class)))
    @ApiResponse(responseCode = "400", description = "요청 불가(errorMessage 확인)", content = @Content(schema = @Schema(implementation = FollowDto.class)))
    @PostMapping("/requests/{followId}/decline")
    public ResponseEntity<FollowDto> declineFollowRequest(Authentication authentication, @PathVariable Long followId) {
        FollowDto followDto = followService.declineFollowRequest(authentication, followId);
        return okOrBadRequest(followDto);
    }

    @Operation(
        summary = "닉네임 검색",
        description = "입력 prefix로 닉네임을 검색. (대소문자 무시)"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "검색 요청 바디",
        required = true,
        content = @Content(schema = @Schema(implementation = RetrieveProfileRequest.class))
    )
    @ApiResponse(responseCode = "200", description = "성공 (빈 배열 반환 - 검색 결과가 없음 or 로그인된 사용자가 아님 or keyword가 없거나 null)", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RetrieveProfileDto.class))))
    @GetMapping("/profiles/search")
    public ResponseEntity<List<RetrieveProfileDto>> retrieveTaBiUser(Authentication authentication, @RequestBody RetrieveProfileRequest retrieveProfileRequest) {
        List<RetrieveProfileDto> retrieveProfileDtoList = followService.retrieveTaBiUser(authentication, retrieveProfileRequest);
        return ResponseEntity.ok(retrieveProfileDtoList);
    }

    private ResponseEntity<FollowDto> okOrBadRequest(FollowDto followDto) {
        if (followDto != null && followDto.getErrorMessage() != null && !followDto.getErrorMessage().isBlank()) {
            return ResponseEntity.badRequest().body(followDto);
        }
        return ResponseEntity.ok(followDto);
    }
}
