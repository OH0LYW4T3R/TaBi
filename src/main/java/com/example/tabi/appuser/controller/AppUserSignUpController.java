package com.example.tabi.appuser.controller;

import com.example.tabi.appuser.service.AppUserService;
import com.example.tabi.appuser.service.AppUserSignUpService;
import com.example.tabi.appuser.vo.AppUserDto;
import com.example.tabi.appuser.vo.AppUserRequest;
import com.example.tabi.appuser.vo.EmailAuthRequest;
import com.example.tabi.appuser.vo.EmailRequest;
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

    @PostMapping("/info")
    public ResponseEntity<?> createUser(@RequestBody AppUserRequest appUserRequest) {
        AppUserDto appUserDto = appUserSignUpService.createAppUser(appUserRequest);
        return ResponseEntity.ok(appUserDto);
    }

    @PostMapping("/code-generation")
    public ResponseEntity<?> codeGenerate(@RequestBody @Valid EmailRequest emailRequest) {
        if (appUserSignUpService.generateEmailAuthenticationCode(emailRequest))
            return ResponseEntity.badRequest().body("Email that already exists");

        return ResponseEntity.ok().body("Code Generated");
    }

    @PostMapping("/code-verification")
    public ResponseEntity<?> codeVerify(@RequestBody @Valid EmailAuthRequest emailAuthRequest) {
        if (!appUserSignUpService.verifyEmailCode(emailAuthRequest))
            return ResponseEntity.badRequest().body("Inaccurate code entry, or never issued or expired");

        return  ResponseEntity.ok().body("Code verified");
    }

}
