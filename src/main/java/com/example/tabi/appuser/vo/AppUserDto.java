package com.example.tabi.appuser.vo;

import com.example.tabi.member.vo.MemberDto;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDto {
    private Long appUserId;
    private String username;
    private LocalDateTime birth;
    private boolean gender;
    private String mobileCarrier;
    private String phoneNumber;
    private boolean agreement;
    private boolean locked;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private MemberDto memberDto;
}
