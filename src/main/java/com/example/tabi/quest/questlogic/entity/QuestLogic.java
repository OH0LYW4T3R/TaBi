package com.example.tabi.quest.questlogic.entity;

import com.example.tabi.quest.questindicating.entity.QuestIndicating;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class QuestLogic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questLogicId;

    @ManyToOne
    @JoinColumn(name = "quest_indicating_id")
    private QuestIndicating questIndicating;
}
