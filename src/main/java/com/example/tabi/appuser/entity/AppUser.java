package com.example.tabi.appuser.entity;

import com.example.tabi.member.entity.Member;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appUserId;
    private String username;
    private LocalDateTime birth;
    // true: 남성, false: 여성
    private boolean gender;
    private String mobileCarrier;
    private String phoneNumber;
    private boolean agreement;
    private boolean locked;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Member member;
}
