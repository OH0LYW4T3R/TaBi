package com.example.tabi.member.entity;

import com.example.tabi.appuser.entity.AppUser;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String email;
    private String password;

    @OneToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;
}
