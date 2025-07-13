package com.example.tabi.appuser.service;

import com.example.tabi.appuser.vo.AppUserDto;
import com.example.tabi.appuser.vo.AppUserRequest;
import org.springframework.security.core.Authentication;

public interface AppUserService {
    AppUserDto getAppUser(Authentication authentication);
    AppUserDto updateAppUser(Authentication authentication, AppUserRequest appUserRequest);
    void deleteAppUser(Authentication authentication);
}
