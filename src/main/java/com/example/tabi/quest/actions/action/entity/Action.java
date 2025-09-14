package com.example.tabi.quest.actions.action.entity;

import com.example.tabi.quest.actions.ActionType;
import com.example.tabi.quest.actions.action.vo.ActionDto;
import com.example.tabi.quest.questlogic.entity.QuestStep;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "action_type", length = 40)
public abstract class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long actionId;

//    private ActionType actionType;

    private String characterImageUrl;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "quest_step_id")
    private QuestStep questStep;

    public ActionDto actionToActionDto() {
        // 나중에 사용시 객체를 생성하거나 꺼내고 -> return actions.stream().map(Action::toDto).toList();
        return new ActionDto(getActionId(), getCharacterImageUrl(), getQuestStep().getQuestStepId());
    }
}
