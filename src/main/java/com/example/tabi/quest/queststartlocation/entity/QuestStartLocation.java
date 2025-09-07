package com.example.tabi.quest.queststartlocation.entity;

import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class QuestStartLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questStartLocationId;

    private String actualLocation;

    private double latitude;
    private double longitude;
    private double altitude;

    @OneToOne
    @JoinColumn(name = "quest_post_id")
    private QuestPost questPost;
}
