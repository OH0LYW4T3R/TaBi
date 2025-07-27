package com.example.tabi.reword.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewordDto {
    private Long rewordId;
    private int experience;
    private boolean type; // true = 일반 보상, false = 특별 보상
    private int creditCardCount;
    private int coin;
}
