package com.example.tabi.myprofile.vo;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyProfileDto {
    private Long myProfileId;
    private String nickName;
    private String profileImageUrl;
    private Integer level;
    private Integer experience;
}

