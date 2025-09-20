package com.example.tabi.quest.quest.entity;

import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.quest.questrunninglocation.entity.QuestRunningLocation;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questId;

    @OneToMany(mappedBy = "quest", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<QuestRunningLocation> questRunningLocations = new ArrayList<>();

    // 관계의 주인
    @OneToOne
    @JoinColumn(name = "quest_post_id")
    private QuestPost questPost;
}
