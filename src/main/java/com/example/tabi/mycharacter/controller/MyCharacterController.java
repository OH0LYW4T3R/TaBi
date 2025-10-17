package com.example.tabi.mycharacter.controller;

import com.example.tabi.mycharacter.service.MyCharacterService;
import com.example.tabi.mycharacter.vo.DrawResultDto;
import com.example.tabi.mycharacter.vo.MyCharacterDto;
import com.example.tabi.mycharacter.vo.MyCharacterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/my-character")
@RequiredArgsConstructor
@Tag(name = "MyCharacter", description = "내 캐릭터 관련 API")
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

    @Operation(
        summary = "상대방 캐릭터 정보 조회",
        description = "상대방 프로필 ID 기준으로 해당 사용자의 보유 캐릭터 정보를 조회.",
        parameters = {
            @Parameter(name = "myProfileId", description = "상대방 MyProfile ID", required = true, example = "42")
        },
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
                description = "MyProfile Not Found",
                content = @Content(schema = @Schema(implementation = String.class))
            )
        }
    )
    @GetMapping("/counterparty-info")
    public ResponseEntity<?> getCounterpartyCharacterInfo(@RequestParam Long myProfileId) {
        MyCharacterDto myCharacterDto = myCharacterService.getMyCharacterForCounterparty(myProfileId);
        if (myCharacterDto == null) {
            return ResponseEntity.badRequest().body("MyProfile Not Found");
        }
        return ResponseEntity.ok(myCharacterDto);
    }

    @Operation(
        summary = "캐릭터 뽑기",
        description = """
            뽑기권 등급에 따라 캐릭터를 랜덤으로 뽑음.<br>
            - NORMAL: 1~3성 범위에서 중복 없이 뽑기<br>
            - ADVANCED: 2~4성 범위에서 중복 없이 뽑기
            """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "뽑기 요청 바디",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = MyCharacterRequest.class),
                examples = {
                    @ExampleObject(
                        name = "일반권 3개",
                        value = """
                            {
                              "drawType": "NORMAL",
                              "count": 3
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "고급권 5개",
                        value = """
                            {
                              "drawType": "ADVANCED",
                              "count": 5
                            }
                            """
                    )
                }
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "뽑기 성공",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = DrawResultDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청(뽑기권 부족, 음수 count 등)",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE
            )
        ),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/draw")
    public ResponseEntity<?> drawCharacters(Authentication authentication, @RequestBody MyCharacterRequest myCharacterRequest) {
        DrawResultDto result = myCharacterService.drawCharacter(authentication, myCharacterRequest);

        if (result.getErrorMessage() != null) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }
}
