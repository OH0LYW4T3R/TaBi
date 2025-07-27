package com.example.tabi.treasurehunt.mytreasurehunt.entity;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.config.PostStatus;
import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class MyTreasureHunt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long myTreasureHuntId;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "treasure_hunt_post_id")
    private TreasureHuntPost treasureHuntPost;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
