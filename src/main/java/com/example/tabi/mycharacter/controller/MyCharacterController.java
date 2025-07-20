package com.example.tabi.mycharacter.controller;

import com.example.tabi.mycharacter.service.MyCharacterService;
import com.example.tabi.mycharacter.vo.MyCharacterDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/my-character")
@RequiredArgsConstructor
public class MyCharacterController {
    private final MyCharacterService myCharacterService;

    @Operation(
    summary = "내 캐릭터 정보 조회",
    description = "인증된 사용자의 보유 캐릭터 정보를 조회.",
    responses = {
        @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MyCharacterDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "AppUser가 존재하지 않음"
        )
    }
)
    @GetMapping("/info")
    public ResponseEntity<?> getMyCharacterInfo(Authentication authentication) {
        MyCharacterDto myCharacterDto = myCharacterService.getMyCharacter(authentication);

        if  (myCharacterDto == null)
            return ResponseEntity.badRequest().body("Not exist AppUser");

        return ResponseEntity.ok(myCharacterDto);
    }
}
