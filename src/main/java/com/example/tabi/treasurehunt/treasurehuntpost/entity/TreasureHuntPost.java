package com.example.tabi.treasurehunt.treasurehuntpost.entity;

import com.example.tabi.postcounter.entity.PostCounter;
import com.example.tabi.reword.entity.Reword;
import com.example.tabi.treasurehunt.mytreasurehunt.entity.MyTreasureHunt;
import com.example.tabi.treasurehunt.treasurehuntpostImage.entity.TreasureHuntPostImage;
import com.example.tabi.treasurehunt.treasurehuntstartlocation.entity.TreasureHuntLocation;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class TreasureHuntPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long treasureHuntPostId;

    private String uploadUserName;
    private String uploadUserProfileUrl;

    private String treasureHuntTitle;
    private String treasureHuntDescription;

    // 다른 사람이 완료했는가
    private boolean termination;
    private boolean locked;
    private boolean pub;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_counter_id")
    private PostCounter postCounter;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reword_id")
    private Reword reword;


    @OneToOne(mappedBy = "treasureHuntPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private TreasureHuntPostImage treasureHuntPostImages;

    @OneToOne(mappedBy = "treasureHuntPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private TreasureHuntLocation treasureHuntStartLocation;

    // 📌 참여 기록 (MyTreasureHunt) - 선택적 양방향
    @OneToMany(mappedBy = "treasureHuntPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MyTreasureHunt> myTreasureHunts;

    private LocalDateTime createdAt;
}
