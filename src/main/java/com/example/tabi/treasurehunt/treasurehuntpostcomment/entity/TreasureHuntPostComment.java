package com.example.tabi.treasurehunt.treasurehuntpostcomment.entity;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class TreasureHuntPostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long treasureHuntPostCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treasure_hunt_post_id")
    private TreasureHuntPost treasureHuntPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private Integer likeCount = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // 부모 댓글 -> Null 이면 최상위 댓글, 그게아니라 특정 값이면 대댓글이며, 부모를 가르킴.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private TreasureHuntPostComment parent;

    // 자식(대댓글)
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("likeCount DESC, createdAt DESC")
    private List<TreasureHuntPostComment> children = new ArrayList<>();
}
