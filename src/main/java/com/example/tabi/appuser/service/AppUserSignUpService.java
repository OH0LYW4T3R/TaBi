package com.example.tabi.appuser.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.vo.AppUserDto;
import com.example.tabi.appuser.vo.AppUserRequest;
import com.example.tabi.appuser.vo.EmailAuthRequest;
import com.example.tabi.appuser.vo.EmailRequest;

public interface AppUserSignUpService {
    AppUserDto createAppUser(AppUserRequest appUserRequest);
    Boolean generateEmailAuthenticationCode(EmailRequest emailRequest);
    Boolean verifyEmailCode(EmailAuthRequest emailAuthRequest);
    void removeExpiredCode();

    void autoCreation(AppUser appUser);
}
