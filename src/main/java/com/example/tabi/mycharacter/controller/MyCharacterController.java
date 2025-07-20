package com.example.tabi.mycharacter.controller;

import com.example.tabi.mycharacter.service.MyCharacterService;
import com.example.tabi.mycharacter.vo.MyCharacterDto;
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

    @GetMapping("/info")
    public ResponseEntity<?> getMyCharacterInfo(Authentication authentication) {
        MyCharacterDto myCharacterDto = myCharacterService.getMyCharacter(authentication);

        if  (myCharacterDto == null)
            return ResponseEntity.badRequest().body("Not exist AppUser");

        return ResponseEntity.ok(myCharacterDto);
    }
}
