package com.example.tabi.appuser.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppUserRequest {
    private String username;
    private LocalDateTime birth;
    private boolean gender;
    private String mobileCarrier;
    private String phoneNumber;
    private boolean agreement;

    private String email;
    private String password;
}
