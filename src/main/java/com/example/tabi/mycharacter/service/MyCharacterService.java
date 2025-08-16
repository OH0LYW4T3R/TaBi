package com.example.tabi.mycharacter.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.mycharacter.vo.MyCharacterDto;
import com.example.tabi.character.entity.Character;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

public interface MyCharacterService {
    void createMyCharacter(AppUser appUser);
    void addMyCharacter(Set<Character> myCharacters, List<Character> addCharacters);

    MyCharacterDto getMyCharacter(Authentication authentication);
}
