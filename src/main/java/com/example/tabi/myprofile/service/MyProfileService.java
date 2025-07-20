package com.example.tabi.myprofile.service;

import com.example.tabi.myprofile.vo.MyProfileDto;
import com.example.tabi.myprofile.vo.ProfileRequest;
import org.springframework.security.core.Authentication;

public interface MyProfileService {
    MyProfileDto createMyProfile(Authentication authentication, ProfileRequest profileRequest);
    MyProfileDto updateMyProfile(Authentication authentication, ProfileRequest profileRequest);
    MyProfileDto getMyProfile(Authentication authentication); // 본인 프로필 조회용

    MyProfileDto retrieveProfile(String nickName); // 다른 사용자 검색용

    String getCharacterImageUrl(String characterId);

    boolean nicknameDuplicateCheck(String nickName);
}
