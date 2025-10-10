package com.example.tabi.mycharacter.vo;

import com.example.tabi.mycharacter.DrawType;
import lombok.Data;

@Data
public class MyCharacterRequest {
    private DrawType drawType;
    private Integer count;
}
