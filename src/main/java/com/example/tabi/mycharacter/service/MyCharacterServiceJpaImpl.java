package com.example.tabi.mycharacter.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.repository.AppUserRepository;
import com.example.tabi.appuser.service.AppUserServiceJpaImpl;
import com.example.tabi.character.entity.Character;
import com.example.tabi.character.repository.CharacterRepository;
import com.example.tabi.member.repository.MemberRepository;
import com.example.tabi.mycharacter.entity.MyCharacter;
import com.example.tabi.mycharacter.repository.MyCharacterRepository;
import com.example.tabi.mycharacter.vo.MyCharacterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MyCharacterServiceJpaImpl implements MyCharacterService {
    private final MemberRepository memberRepository;
    private final AppUserRepository appUserRepository;
    private final CharacterRepository characterRepository;
    private final MyCharacterRepository myCharacterRepository;

    @Override
    public void createMyCharacter(AppUser appUser) {
        if (myCharacterRepository.existsByAppUser(appUser))
            return;

        Character character = characterRepository.findByCharacterNameAndRank("owl", 1);
        Character character1 = characterRepository.findByCharacterNameAndRank("squirrel", 1);
        Set<Character> myCharacters = new HashSet<>();
        myCharacters.add(character);
        myCharacters.add(character1);

        MyCharacter myCharacter = new MyCharacter();
        myCharacter.setMyCharacters(myCharacters);
        myCharacter.setAppUser(appUser);

        myCharacterRepository.save(myCharacter);

        myCharacterToMyCharacterDto(myCharacter);
    }

    @Override
    public void addMyCharacter(Set<Character> myCharacters, List<Character> addCharacters) {

    }

    @Override
    public MyCharacterDto getMyCharacter(Authentication authentication) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();

        MyCharacter myCharacter = myCharacterRepository.findByAppUser(appUser);

        return myCharacterToMyCharacterDto(myCharacter);
    }

    public static MyCharacterDto myCharacterToMyCharacterDto(MyCharacter myCharacter) {
        return new MyCharacterDto(myCharacter.getMyCharacterId(), myCharacter.getMyCharacters(), myCharacter.getAppUser().getAppUserId());
    }
}
