package com.example.tabi.myprofile.controller;

import com.example.tabi.myprofile.entity.MyProfile;
import com.example.tabi.myprofile.service.MyProfileService;
import com.example.tabi.myprofile.vo.MyProfileDto;
import com.example.tabi.myprofile.vo.ProfileRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/my-profile")
@RequiredArgsConstructor
public class MyProfileController {
    private final MyProfileService myProfileService;

    @Operation(
        summary = "닉네임 중복 확인",
        description = "사용자가 입력한 닉네임이 이미 존재하는지 확인.<br>닉네임 부분만 채울 것.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProfileRequest.class)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "사용 가능한 닉네임"),
            @ApiResponse(responseCode = "400", description = "이미 존재하는 닉네임")
        }
    )
    @PostMapping("/nickname-duplication-check")
    public ResponseEntity<?> duplicationCheck(@RequestBody ProfileRequest profileRequest){
        if (myProfileService.nicknameDuplicateCheck(profileRequest.getNickName()))
            return ResponseEntity.badRequest().body("Already exists nickname");

        return ResponseEntity.ok("Available");
    }

    @Operation(
        summary = "프로필 생성",
        description = "사용자 인증 정보를 기반으로 새로운 프로필을 생성.<br>닉네임 중복 또는 이미 생성된 경우 실패.<br>Image Url 부분은 charaterId.png 로만 보낼것 (ex. owl_1.png)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProfileRequest.class)
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "프로필 생성 성공",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MyProfileDto.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "이미 생성된 사용자 또는 닉네임 중복")
        }
    )
    @PostMapping("/creation")
    public ResponseEntity<?> createMyProfile(@RequestBody ProfileRequest profileRequest, Authentication authentication) {
        MyProfileDto myProfileDto = myProfileService.createMyProfile(authentication, profileRequest);

        if  (myProfileDto == null)
            return ResponseEntity.badRequest().body("Already created or Nickname Duplicated");

        return ResponseEntity.ok(myProfileDto);
    }

    @Operation(
        summary = "프로필 조회",
        description = "로그인된 사용자 정보를 바탕으로 자신의 프로필 정보를 조회.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "프로필 조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MyProfileDto.class)
                )
            ),
            @ApiResponse(responseCode = "404", description = "프로필이 존재하지 않음")
        }
    )
    @GetMapping("/retrieval")
    public ResponseEntity<?> getMyProfile(Authentication authentication) {
        MyProfileDto myProfileDto = myProfileService.getMyProfile(authentication);

        if (myProfileDto == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(myProfileDto);
    }

    @Operation(
        summary = "캐릭터 이미지 조회",
        description = "characterId에 해당하는 캐릭터 이미지 리소스를 반환. <br>*주의* - 해당 이미지 조회는 /api/my-profile 을 제외할것 (ex. domain address/profile-characters/characterId)",
        parameters = {
            @Parameter(
                name = "characterId",
                description = "조회할 캐릭터 ID (ex. owl_1.png)<br>CharacterID 파일명 포맷: {characterName]_{star}.png",
                required = true,
                in = ParameterIn.PATH,
                example = "character_001"
            )
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "이미지 반환 성공",
                content = @Content(mediaType = "image/png")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "이미지가 존재하지 않음"
            )
        }
    )
    @PostMapping("/profile-characters/{characterId}")
    public ResponseEntity<Void> imageDoc(@PathVariable String characterId) {
        return ResponseEntity.noContent().build();
    }
}
