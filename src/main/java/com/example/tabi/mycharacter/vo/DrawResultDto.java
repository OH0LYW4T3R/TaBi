package com.example.tabi.mycharacter.vo;

import com.example.tabi.character.entity.Character;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrawResultDto {
    private List<Character> drawCharacters;
    private String errorMessage;

    public static DrawResultDto createDrawResultDto(List<Character> drawCharacters, String errorMessage) {
        return new DrawResultDto(drawCharacters, errorMessage);
    }
}
