package com.example.tabi.myprofile.entity;

import com.example.tabi.appuser.entity.AppUser;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MyProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long myProfileId;

    @Column(nullable = false)
    private String nickName;

    private String profileImageUrl;

    private Integer level;

    private Integer experience;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;
}
