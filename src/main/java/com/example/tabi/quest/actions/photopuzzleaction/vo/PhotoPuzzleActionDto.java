package com.example.tabi.quest.actions.photopuzzleaction.vo;

import com.example.tabi.quest.actions.ActionType;
import com.example.tabi.quest.actions.action.vo.ActionDto;
import com.example.tabi.quest.actions.hint.entity.Hint;
import com.example.tabi.quest.actions.hint.vo.HintDto;
import com.example.tabi.quest.actions.photopuzzleaction.entity.PhotoKeyword;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhotoPuzzleActionDto extends ActionDto {
    private HintDto hintDto;
    private List<PhotoKeywordDto> photoKeywordDtos;

     public PhotoPuzzleActionDto(Long actionId, String characterImageUrl, Long questStepId, HintDto hintDto, List<PhotoKeywordDto> photoKeywordDtos) {
        super(actionId, characterImageUrl, questStepId);
        this.hintDto = hintDto;
        this.photoKeywordDtos = photoKeywordDtos;
    }
}
