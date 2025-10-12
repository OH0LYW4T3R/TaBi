package com.example.tabi.follow.entity;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.follow.vo.FollowStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint(name = "uq_follow_follower_followee", columnNames = {"follower_id", "followee_id"}))
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followId;

    // AppUser N : 1 Follow
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "follower_id", nullable = false)
    private AppUser follower;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "followee_id", nullable = false)
    private AppUser followee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private FollowStatus followStatus = FollowStatus.REQUESTED;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
