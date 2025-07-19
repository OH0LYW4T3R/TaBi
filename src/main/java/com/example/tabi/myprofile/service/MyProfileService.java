package com.example.tabi.myprofile.service;

import com.example.tabi.myprofile.vo.MyProfileDto;
import com.example.tabi.myprofile.vo.ProfileRequest;
import org.springframework.security.core.Authentication;

public interface MyProfileService {
    MyProfileDto createMyProfile(ProfileRequest profileRequest);
    MyProfileDto updateMyProfile(Authentication authentication, ProfileRequest profileRequest);
    MyProfileDto getMyProfileById(Authentication authentication);

    String getCharacterImageUrl(String characterId);

    boolean nicknameDuplicateCheck(String nickName);
}
