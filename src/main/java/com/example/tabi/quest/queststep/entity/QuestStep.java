package com.example.tabi.quest.queststep.entity;

import com.example.tabi.quest.actions.action.entity.Action;
import com.example.tabi.quest.questindicating.entity.QuestIndicating;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class QuestStep {
    // Action이 추가되는게 아니라 해당 클래스가 추가 되어야 함.
    // 추후 스텝에 대한 메타데이터가 필요할 수 도 있음.
    // ex) QuestStep의 어떤 Action에 사용자가 몰려있는지..
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questStepId;

    @ManyToOne
    @JoinColumn(name = "quest_indicating_id")
    private QuestIndicating questIndicating;

    @OneToOne(mappedBy = "questStep", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Action action;
}
