package com.example.tabi.quest.myquestplay.trace.questcurrentpoint.entity;

import com.example.tabi.quest.myquestplay.entity.MyQuestPlay;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class QuestCurrentPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questCurrentPointId;

    private Long currentQuestRunningLocationId;
    private Integer currentQuestRunningLocationIndex;

    // Numbering은 0-based
    private Long currentActionId;
    private Integer currentActionIndex;

    private Long endQuestRunningLocationId;
    private Integer endQuestRunningLocationIndex;

    private Long endActionId;
    private Integer endActionIndex;

    @OneToOne
    @JoinColumn(name = "my_quest_play_id")
    private MyQuestPlay myQuestPlay;
}
