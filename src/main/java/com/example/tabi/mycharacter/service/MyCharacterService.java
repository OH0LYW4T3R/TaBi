package com.example.tabi.mycharacter.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.mycharacter.vo.DrawResultDto;
import com.example.tabi.mycharacter.vo.MyCharacterDto;
import com.example.tabi.character.entity.Character;
import com.example.tabi.mycharacter.vo.MyCharacterRequest;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

public interface MyCharacterService {
    void createMyCharacter(AppUser appUser);
    void addMyCharacter(Set<Character> myCharacters, List<Character> addCharacters);

    DrawResultDto drawCharacter(Authentication authentication, MyCharacterRequest myCharacterRequest);
    MyCharacterDto getMyCharacter(Authentication authentication);
}
