package com.example.tabi.follow.vo;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.myprofile.entity.MyProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveProfileDto {
    private Long appUserId;
    private String nickname;
    private String profileImageUrl;
    private String errorMessage;

    public static RetrieveProfileDto createRetrieveProfileDto(MyProfile myProfile) {
        return new RetrieveProfileDto(
                myProfile.getAppUser().getAppUserId(),
                myProfile.getNickName(),
                myProfile.getProfileImageUrl(),
                null
        );
    }
}
