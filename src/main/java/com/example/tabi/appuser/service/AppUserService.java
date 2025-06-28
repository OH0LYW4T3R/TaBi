package com.example.tabi.appuser.service;

import com.example.tabi.appuser.vo.AppUserDto;
import com.example.tabi.appuser.vo.AppUserRequest;
import com.example.tabi.appuser.vo.RedundancyCheckRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

public interface AppUserService {
    AppUserDto createAppUser(AppUserRequest appUserRequest);
    AppUserDto getAppUser(Authentication authentication);
    AppUserDto updateAppUser(Authentication authentication, AppUserRequest appUserRequest);
    void deleteAppUser(Authentication authentication);

    Boolean emailRedundancyCheck(RedundancyCheckRequest redundancyCheckRequest);
    Boolean phoneNumberRedundancyCheck(RedundancyCheckRequest redundancyCheckRequest);
}
