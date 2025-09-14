package com.example.tabi.quest.actions.photopuzzleaction.entity;

import com.example.tabi.quest.actions.action.entity.Action;
import com.example.tabi.quest.actions.hint.entity.Hint;
import com.example.tabi.quest.actions.hint.vo.HintDto;
import com.example.tabi.quest.actions.inputpuzzleaction.vo.InputPuzzleActionDto;
import com.example.tabi.quest.actions.photopuzzleaction.vo.PhotoKeywordDto;
import com.example.tabi.quest.actions.photopuzzleaction.vo.PhotoPuzzleActionDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter @Setter
@DiscriminatorValue("PhotoPuzzleAction")
public class PhotoPuzzleAction extends Action {
    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "hint_id")
    private Hint hint;

    @OneToMany(mappedBy = "photoPuzzleAction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhotoKeyword> photoKeywords = new ArrayList<>();

    @Override
    public PhotoPuzzleActionDto actionToActionDto() {
        List<PhotoKeywordDto> photoKeywordDtos = photoKeywords.stream().map(PhotoKeywordDto::photoKeywordToPhotoKeywordDto).collect(Collectors.toList());
        return new PhotoPuzzleActionDto(getActionId(), getCharacterImageUrl(), getQuestStep().getQuestStepId(), HintDto.hintToHintDto(hint), photoKeywordDtos);
    }
}
