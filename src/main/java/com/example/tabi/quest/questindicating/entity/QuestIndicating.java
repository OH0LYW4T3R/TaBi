package com.example.tabi.quest.questindicating.entity;

import com.example.tabi.quest.questlogic.entity.QuestStep;
import com.example.tabi.quest.questrunninglocation.entity.QuestRunningLocation;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class QuestIndicating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questIndicatingId;

    private Integer actionCount;

    boolean talkingAction;
    boolean stayingAction;
    boolean puzzleAction;
    boolean walkingAction;

    @OneToMany(mappedBy = "questIndicating", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<QuestStep> questSteps; // 퀘스트의 순서가 담김

    // 관계의 주인
    @OneToOne
    @JoinColumn(name = "quest_running_location_id")
    private QuestRunningLocation questRunningLocation;
}
