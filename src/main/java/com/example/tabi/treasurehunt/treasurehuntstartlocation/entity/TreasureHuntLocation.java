package com.example.tabi.treasurehunt.treasurehuntstartlocation.entity;

import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TreasureHuntLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long treasureHuntLocationId;

    private String actualLocation;   // 서버용
    private String indicateLocation; // 사용자용

    private double latitude;
    private double longitude;
    private double altitude;

    @OneToOne
    @JoinColumn(name = "treasure_hunt_post_id")
    private TreasureHuntPost treasureHuntPost;
}
