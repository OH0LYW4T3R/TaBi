package com.example.tabi.quest.myquestplay.trace.questsavepoint.entity;

import com.example.tabi.quest.myquestplay.entity.MyQuestPlay;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class QuestSavePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questSavePointId;

    private Long saveQuestRunningLocationId;
    private Integer saveQuestRunningLocationNumber;

    private Long saveActionId;
    private Integer saveActionNumber;

    @OneToOne
    @JoinColumn(name = "my_quest_play_id")
    private MyQuestPlay myQuestPlay;
}
