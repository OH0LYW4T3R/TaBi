package com.example.tabi.appuser.controller;

import com.example.tabi.appuser.service.AppUserService;
import com.example.tabi.appuser.service.AppUserSignUpService;
import com.example.tabi.appuser.vo.AppUserDto;
import com.example.tabi.appuser.vo.AppUserRequest;
import com.example.tabi.appuser.vo.EmailAuthRequest;
import com.example.tabi.appuser.vo.EmailRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app-user/sign-up")
@RequiredArgsConstructor
public class AppUserSignUpController {
    private final AppUserSignUpService appUserSignUpService;

    @Operation(
        summary = "유저 생성",
        description = "이메일 인증을 마친 유저 정보를 모두 취합하여 전송 후, 사용자 생성",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AppUserRequest.class)
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "유저 생성 성공",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AppUserDto.class)
                )
            )
        }
    )
    @PostMapping("/info")
    public ResponseEntity<?> createUser(@RequestBody AppUserRequest appUserRequest) {
        AppUserDto appUserDto = appUserSignUpService.createAppUser(appUserRequest);
        return ResponseEntity.ok(appUserDto);
    }

    @Operation(
        summary = "이메일 인증 코드 발급",
        description = """
            이메일 중복 여부를 확인한 후 인증 코드를 생성하여 사용자의 이메일로 전송<br>
            조건 - 이미 가입된 이메일은 인증 코드를 발급받을 수 없음.
            """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EmailRequest.class)
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "인증 코드 발급 성공",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = String.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "이메일이 이미 존재함"
            )
        }
    )
    @PostMapping("/code-generation")
    public ResponseEntity<?> codeGenerate(@RequestBody @Valid EmailRequest emailRequest) {
        if (appUserSignUpService.generateEmailAuthenticationCode(emailRequest))
            return ResponseEntity.badRequest().body("Email that already exists");

        return ResponseEntity.ok().body("Code Generated");
    }

    @Operation(
        summary = "이메일 인증 코드 검증",
        description = """
            사용자가 제출한 이메일과 인증 코드가 일치하는지 검증.<br>
            조건 1 - 발급하고 인증 요청.<br>
            조건 2 - 만료시간 내에 인증코드 작성.<br>
            조건 3 - 이메일로 보낸 코드와 같은 인증코드로 작성.
            """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EmailAuthRequest.class)
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "이메일 인증 성공",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = String.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "인증 실패 (코드 불일치, 만료, 미발급 등)"
            )
        }
    )
    @PostMapping("/code-verification")
    public ResponseEntity<?> codeVerify(@RequestBody @Valid EmailAuthRequest emailAuthRequest) {
        if (!appUserSignUpService.verifyEmailCode(emailAuthRequest))
            return ResponseEntity.badRequest().body("Inaccurate code entry, or never issued or expired");

        return  ResponseEntity.ok().body("Code verified");
    }
}
