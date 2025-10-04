package com.example.tabi.quest.myquestplay.trace.questhintsave.entity;

import com.example.tabi.quest.actions.ActionType;
import com.example.tabi.quest.myquestplay.entity.MyQuestPlay;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class QuestHintSave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questHintSaveId;

    private Long questRunningLocationId; // 밑에 있는 순서 객체의 원래 id
    private Integer questRunningLocationNumber;

    private Long actionId; // 밑에 있는 순서 객체의 원래 id
    private Integer actionNumber;
    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    private Long hintId; // 밑에 있는 순서 객체의 원래 id
    private boolean hintOneLocked;
    private boolean hintTwoLocked;
    private boolean hintThreeLocked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "my_quest_play_id")
    private MyQuestPlay myQuestPlay;
}
