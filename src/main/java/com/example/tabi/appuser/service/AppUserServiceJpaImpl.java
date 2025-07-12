package com.example.tabi.appuser.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.repository.AppUserRepository;
import com.example.tabi.appuser.vo.AppUserDto;
import com.example.tabi.appuser.vo.AppUserRequest;
import com.example.tabi.appuser.vo.RedundancyCheckRequest;
import com.example.tabi.login.CustomUserDetails;
import com.example.tabi.member.entity.Member;
import com.example.tabi.member.repository.MemberRepository;
import com.example.tabi.member.vo.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserServiceJpaImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUserDto createAppUser(AppUserRequest appUserRequest) {
        AppUser appUser = new AppUser();
        appUser.setUsername(appUserRequest.getUsername());
        appUser.setBirth(appUserRequest.getBirth());
        appUser.setGender(appUserRequest.isGender());
        appUser.setMobileCarrier(appUserRequest.getMobileCarrier());
        appUser.setPhoneNumber(appUserRequest.getPhoneNumber());
        log.info("Agreement : {}", appUserRequest.isAgreement());
        System.out.println(appUserRequest.isAgreement());
        appUser.setAgreement(appUserRequest.isAgreement());
        appUser.setLocked(false);

        Member member = new Member();
        member.setEmail(appUserRequest.getEmail());
        member.setPassword(passwordEncoder.encode(appUserRequest.getPassword()));

        appUser.setMember(member);
        member.setAppUser(appUser);

        appUserRepository.save(appUser);
        memberRepository.save(member);

        return appUserToAppUserDto(appUser);
    }

    @Override
    public AppUserDto getAppUser(Authentication authentication) {
        Optional<AppUser> appUser = authenticationToAppUser(authentication, this.memberRepository, this.appUserRepository);

        if (appUser.isEmpty())
            return null;

        return appUserToAppUserDto(appUser.get());
    }

    @Override
    public AppUserDto updateAppUser(Authentication authentication, AppUserRequest appUserRequest) {
        return null;
    }

    @Override
    public void deleteAppUser(Authentication authentication) {

    }

    @Override
    public Boolean phoneNumberRedundancyCheck(RedundancyCheckRequest redundancyCheckRequest) {
        return null;
    }

    @Override
    public Boolean emailRedundancyCheck(RedundancyCheckRequest redundancyCheckRequest) {
        return null;
    }

    public static AppUserDto appUserToAppUserDto(AppUser appUser) {
        MemberDto memberDto = new  MemberDto(appUser.getMember().getMemberId(), appUser.getMember().getEmail(), appUser.getMember().getPassword());
        AppUserDto appUserDto = new AppUserDto(appUser.getAppUserId(), appUser.getUsername(), appUser.getBirth(), appUser.isGender(), appUser.getMobileCarrier(), appUser.getPhoneNumber(), appUser.isAgreement(), appUser.isLocked(), appUser.getCreatedAt(), appUser.getUpdatedAt(), memberDto);

        return appUserDto;
    }

    public static Optional<AppUser> authenticationToAppUser(Authentication authentication, MemberRepository memberRepository, AppUserRepository appUserRepository) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        if (customUserDetails == null) {
            return Optional.empty();
        }

        Member member = memberRepository.findByEmail(customUserDetails.getEmail());
        return appUserRepository.findById(member.getAppUser().getAppUserId());
    }
}
