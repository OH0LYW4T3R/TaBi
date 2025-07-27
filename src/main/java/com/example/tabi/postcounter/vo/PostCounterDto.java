package com.example.tabi.postcounter.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCounterDto {
    private Long postCounterId;
    private int likeCount;
    private int playCount;
    private int shareCount;
    private int commentCount;
    private int reportCount;
}
