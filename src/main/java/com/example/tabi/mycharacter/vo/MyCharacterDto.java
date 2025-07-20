package com.example.tabi.mycharacter.vo;

import com.example.tabi.character.entity.Character;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyCharacterDto {
     private Long myCharacterId;
     private Set<Character> characters;

     private Long appUserId;
}
