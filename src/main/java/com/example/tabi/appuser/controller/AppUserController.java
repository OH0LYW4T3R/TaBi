package com.example.tabi.appuser.controller;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.service.AppUserService;
import com.example.tabi.appuser.vo.AppUserDto;
import com.example.tabi.appuser.vo.AppUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app-user")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "AppUser", description = "유저 관련 API")
public class AppUserController {
    private final AppUserService appUserService;


    @Operation(
        summary = "내 사용자 정보 조회",
        description = "로그인된 사용자의 AppUser 정보를 조회.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AppUserDto.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "인증되지 않은 사용자"
            )
        }
    )
    @GetMapping("/my-info")
    public ResponseEntity<?> getMyInfo(Authentication authentication) {
        log.info("getMyInfo");
        AppUserDto appUserDto = appUserService.getAppUser(authentication);
        return ResponseEntity.ok(appUserDto);
    }
}
