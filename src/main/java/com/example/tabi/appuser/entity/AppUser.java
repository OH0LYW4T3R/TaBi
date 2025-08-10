package com.example.tabi.appuser.entity;

import com.example.tabi.member.entity.Member;
import com.example.tabi.mycharacter.entity.MyCharacter;
import com.example.tabi.myprofile.entity.MyProfile;
import com.example.tabi.treasurehunt.mytreasurehunt.entity.MyTreasureHunt;
import com.example.tabi.treasurehunt.mytreasurehuntplay.entity.MyTreasureHuntPlay;
import com.example.tabi.treasurehunt.treasurehuntpostcomment.entity.TreasureHuntPostComment;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private MyProfile myProfile;
    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private MyCharacter myCharacter;


    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MyTreasureHunt> myTreasureHunts = new ArrayList<>();

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MyTreasureHuntPlay> myTreasureHuntPlays = new ArrayList<>();

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TreasureHuntPostComment> treasureHuntPostComments = new ArrayList<>();
}
