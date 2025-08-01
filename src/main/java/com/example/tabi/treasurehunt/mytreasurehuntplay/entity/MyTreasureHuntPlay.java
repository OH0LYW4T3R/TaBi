package com.example.tabi.treasurehunt.mytreasurehuntplay.entity;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.treasurehunt.mytreasurehuntplay.PlayStatus;
import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class MyTreasureHuntPlay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long myTreasureHuntPlayId;

    // 양방향 구성
    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    // 단방향 구성 (반대측에서 굳이 상대방이 실행중인 세부사항을 알필요는 없음)
    @ManyToOne
    @JoinColumn(name = "treasure_hunt_post_id")
    private TreasureHuntPost treasureHuntPost;

    private PlayStatus playStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
