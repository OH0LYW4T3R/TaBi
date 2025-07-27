package com.example.tabi.treasurehunt.treasurehuntpostImage.entity;

import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TreasureHuntPostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long treasureHuntPostImageId;

    private String imageUrl;

    @OneToOne
    @JoinColumn(name = "treasure_hunt_post_id")
    private TreasureHuntPost treasureHuntPost;
}
