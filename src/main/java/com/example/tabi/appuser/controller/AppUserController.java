package com.example.tabi.appuser.controller;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.service.AppUserService;
import com.example.tabi.appuser.vo.AppUserDto;
import com.example.tabi.appuser.vo.AppUserRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app-user")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "AppUser", description = "유저 관련 API")
public class AppUserController {
    private final AppUserService appUserService;

    @PostMapping("/initial/users")
    public ResponseEntity<?> createUser(@RequestBody AppUserRequest appUserRequest) {
        log.info("Agreement {}", appUserRequest.isAgreement());
        AppUserDto appUserDto = appUserService.createAppUser(appUserRequest);
        return ResponseEntity.ok(appUserDto);
    }
}
