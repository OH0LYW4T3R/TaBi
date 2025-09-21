package com.example.tabi.quest.actions.hint.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HintRequest {
    private String hintOne;
    private String hintTwo;
    private String hintThree;
}
