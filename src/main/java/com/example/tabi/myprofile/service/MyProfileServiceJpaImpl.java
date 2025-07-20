package com.example.tabi.myprofile.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.repository.AppUserRepository;
import com.example.tabi.appuser.service.AppUserServiceJpaImpl;
import com.example.tabi.member.repository.MemberRepository;
import com.example.tabi.myprofile.entity.MyProfile;
import com.example.tabi.myprofile.repository.MyProfileRepository;
import com.example.tabi.myprofile.vo.MyProfileDto;
import com.example.tabi.myprofile.vo.ProfileRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyProfileServiceJpaImpl implements MyProfileService {
    private final AppUserRepository appUserRepository;
    private final MemberRepository memberRepository;
    private final MyProfileRepository myProfileRepository;

    @Override
    @Transactional
    public MyProfileDto createMyProfile(Authentication authentication, ProfileRequest profileRequest) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return null;

        if (nicknameDuplicateCheck(profileRequest.getNickName()))
            return null;

        AppUser appUser = optionalAppUser.get();

        if (myProfileRepository.existsByAppUser(appUser))
            return null; // 이미 만들어진것이 있다면 생성 불가

        MyProfile myProfile = new MyProfile();
        myProfile.setAppUser(appUser);
        myProfile.setNickName(profileRequest.getNickName());
        myProfile.setProfileImageUrl(getCharacterImageUrl(profileRequest.getProfileImageUrl()));
        myProfile.setLevel(1);
        myProfile.setExperience(0);

        myProfileRepository.save(myProfile);

        return myProfileToMyProfileDto(myProfile);
    }

    @Override
    public MyProfileDto updateMyProfile(Authentication authentication, ProfileRequest profileRequest) {
        return null;
    }

    @Override
    public MyProfileDto getMyProfile(Authentication authentication) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();
        Optional<MyProfile> optionalMyProfile = myProfileRepository.findByAppUser(appUser);

        // 생성된 프로필이 없으면 null
        return optionalMyProfile.map(MyProfileServiceJpaImpl::myProfileToMyProfileDto).orElse(null);
    }

    @Override
    public MyProfileDto retrieveProfile(String nickName) {
        return null;
    }

    @Override
    public String getCharacterImageUrl(String characterId) {
        return "/profile-characters/" + characterId;
    }

    @Override
    public boolean nicknameDuplicateCheck(String nickName) {
        return myProfileRepository.existsByNickName(nickName);
    }

    public static MyProfileDto myProfileToMyProfileDto(MyProfile myProfile) {
        return new MyProfileDto(myProfile.getMyProfileId(), myProfile.getNickName(), myProfile.getProfileImageUrl(), myProfile.getLevel(), myProfile.getLevel());
    }
}
