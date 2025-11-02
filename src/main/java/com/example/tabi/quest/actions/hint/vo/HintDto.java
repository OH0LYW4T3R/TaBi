package com.example.tabi.quest.actions.hint.vo;

import com.example.tabi.quest.actions.hint.entity.Hint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HintDto {
    private Long hintId;

    private String hintOne;
    private Integer hintOnePrice;

    private String hintTwo;
    private Integer hintTwoPrice;

    private String hintThree;
    private Integer hintThreePrice;

    public static HintDto hintToHintDto(Hint hint) {
        return new HintDto(hint.getHintId(), hint.getHintOne(), hint.getHintOnePrice(), hint.getHintTwo(), hint.getHintTwoPrice(), hint.getHintThree(), hint.getHintThreePrice());
    }
}
