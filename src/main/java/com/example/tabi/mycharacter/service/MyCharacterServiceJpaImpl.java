package com.example.tabi.mycharacter.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.repository.AppUserRepository;
import com.example.tabi.appuser.service.AppUserServiceJpaImpl;
import com.example.tabi.character.entity.Character;
import com.example.tabi.character.repository.CharacterRepository;
import com.example.tabi.member.repository.MemberRepository;
import com.example.tabi.mycharacter.DrawType;
import com.example.tabi.mycharacter.entity.MyCharacter;
import com.example.tabi.mycharacter.repository.MyCharacterRepository;
import com.example.tabi.mycharacter.vo.DrawResultDto;
import com.example.tabi.mycharacter.vo.MyCharacterDto;
import com.example.tabi.mycharacter.vo.MyCharacterRequest;
import com.example.tabi.myinventory.entity.MyInventory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

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

//        myCharacterToMyCharacterDto(myCharacter);
    }

    @Override
    public void addMyCharacter(Set<Character> myCharacters, List<Character> addCharacters) {
        Set<String> existingKeys = new HashSet<>();

        for (Character owned : myCharacters) {
            if (owned == null) continue;

            String name = owned.getCharacterName();
            Integer rank = owned.getRank();

            if (name == null || rank == null) continue;

            existingKeys.add(name + "|" + rank);
        }

        for (Character candidate : addCharacters) {
            if (candidate == null) continue;

            String name = candidate.getCharacterName();
            Integer rank = candidate.getRank();

            if (name == null || rank == null) continue;

            String key = name + "|" + rank;

            if (!existingKeys.contains(key)) {
                myCharacters.add(candidate);
                existingKeys.add(key);
            }
        }
    }

    @Override
    @Transactional
    public DrawResultDto drawCharacter(Authentication authentication, MyCharacterRequest myCharacterRequest) {
        DrawResultDto drawResultForErrorDto = new DrawResultDto();
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty()) {
            drawResultForErrorDto.setErrorMessage("No user found");
            return drawResultForErrorDto;
        }

        if (myCharacterRequest.getCount() <= 0) {
            drawResultForErrorDto.setErrorMessage("Count must be a positive number");
            return drawResultForErrorDto;
        }

        AppUser appUser = optionalAppUser.get();
        Set<Character> myCharacters = appUser.getMyCharacter().getMyCharacters();
        MyInventory myInventory = appUser.getMyInventory();

        Integer advancedDrawCardCount = myInventory.getUniqueCreditCard();
        Integer normalDrawCardCount = myInventory.getNormalCreditCard();

        System.out.println(normalDrawCardCount);

        List<Character> resultCharacters;
        switch (myCharacterRequest.getDrawType()) {
            case NORMAL -> {
                if (normalDrawCardCount < myCharacterRequest.getCount()) {
                    drawResultForErrorDto.setErrorMessage("You have not enough credit cards");
                    return drawResultForErrorDto;
                }
                resultCharacters = drawRandomCharacters(DrawType.NORMAL, myCharacterRequest.getCount());
                myInventory.setNormalCreditCard(normalDrawCardCount - resultCharacters.size());
            }
            case ADVANCED -> {
                if (advancedDrawCardCount < myCharacterRequest.getCount()) {
                    drawResultForErrorDto.setErrorMessage("You have not enough credit cards");
                    return drawResultForErrorDto;
                }
                resultCharacters = drawRandomCharacters(DrawType.ADVANCED, myCharacterRequest.getCount());
                myInventory.setUniqueCreditCard(advancedDrawCardCount - resultCharacters.size()); // ★ 고급권 차감
            }
            default -> {
                drawResultForErrorDto.setErrorMessage("Unsupported draw type");
                return drawResultForErrorDto;
            }
        }

        addMyCharacter(myCharacters, resultCharacters);

        return DrawResultDto.createDrawResultDto(resultCharacters, null);
    }

    @Override
    public MyCharacterDto getMyCharacter(Authentication authentication) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty()) {
            MyCharacterDto myCharacterDto = new MyCharacterDto();
            myCharacterDto.setErrorMessage("No user found");
            return myCharacterDto;
        }

        AppUser appUser = optionalAppUser.get();

        MyCharacter myCharacter = myCharacterRepository.findByAppUser(appUser);

        return myCharacterToMyCharacterDto(myCharacter, null);
    }

    private List<Character> drawRandomCharacters(DrawType drawType, int count) {
        // ADVANCED: 2~4, NORMAL: 1~3
        final int minRank = (drawType == DrawType.ADVANCED) ? 2 : 1;
        final int maxRank = (drawType == DrawType.ADVANCED) ? 4 : 3;

        Set<String> excludedFileNames = Set.of("owl_1.png", "squirrel_1.png");
        List<Character> allCharacters = characterRepository.findAll();

        List<Character> candidateList = allCharacters.stream()
                .filter(character -> {
                    // minRank ~ maxRank 사이 추출
                    Integer rank = character.getRank();
                    return rank != null && rank >= minRank && rank <= maxRank;
                })
                .filter(character -> {
                    // 기본 캐릭터 제외
                    String characterURL = character.getCharacterURL();
                    if (characterURL == null) return true;

                    for (String fileName : excludedFileNames)
                        if (characterURL.endsWith(fileName)) return false;

                    return true;
                }).toList();

        if (count <= 0 || candidateList.isEmpty())
            return Collections.emptyList();

        int drawCount = Math.min(count, candidateList.size());
        List<Character> shuffledCandidates = new ArrayList<>(candidateList);
        Collections.shuffle(shuffledCandidates);
        return new ArrayList<>(shuffledCandidates.subList(0, drawCount));
    }

    public static MyCharacterDto myCharacterToMyCharacterDto(MyCharacter myCharacter, String errorMessage) {
        return new MyCharacterDto(myCharacter.getMyCharacterId(), myCharacter.getMyCharacters(), errorMessage, myCharacter.getAppUser().getAppUserId());
    }
}
