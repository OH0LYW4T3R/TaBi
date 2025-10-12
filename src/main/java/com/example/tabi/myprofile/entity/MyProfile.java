package com.example.tabi.myprofile.entity;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.myprofile.vo.FollowPolicy;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class MyProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long myProfileId;

    @Column(nullable = false, unique = true)
    private String nickName;
    private String profileImageUrl;

    private Integer level;
    private Integer experience;

    private boolean privateProfile = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private FollowPolicy followPolicy = FollowPolicy.AUTO_ACCEPT;

    private long followerCount = 0L;
    private long followingCount = 0L;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;
}
