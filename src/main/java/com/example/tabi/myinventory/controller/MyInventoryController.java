package com.example.tabi.myinventory.controller;

import com.example.tabi.myinventory.entity.MyInventory;
import com.example.tabi.myinventory.service.MyInventoryService;
import com.example.tabi.myinventory.vo.MyInventoryDto;
import io.swagger.v3.oas.annotations.Operation;
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

@RestController
@RequestMapping("/api/my-inventory")
@RequiredArgsConstructor
@Tag(name = "MyInventory", description = "내 인벤토리 관련 API")
public class MyInventoryController {
    private final MyInventoryService myInventoryService;

    @Operation(
            summary = "내 인벤토리 조회",
            description = "로그인한 사용자의 인벤토리 정보를 조회. (코인, 캐릭터 뽑기권 등)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "인벤토리 조회 성공",
            content = @Content(schema = @Schema(implementation = MyInventoryDto.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "인벤토리가 존재하지 않음"
    )
    @GetMapping("/read")
    public ResponseEntity<?> getMyInventory(Authentication authentication) {
        MyInventoryDto myInventoryDto = myInventoryService.getMyInventory(authentication);

        if (myInventoryDto == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(myInventoryDto);
    }
}
