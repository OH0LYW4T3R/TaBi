package com.example.tabi.quest.actions.photopuzzleaction.vo;

import com.example.tabi.quest.actions.photopuzzleaction.entity.PhotoKeyword;
import com.example.tabi.quest.actions.photopuzzleaction.entity.PhotoPuzzleAction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoKeywordDto {
    private Long photoKeywordId;

    private String keyword;

    private Long photoPuzzleActionId;

    public static PhotoKeywordDto photoKeywordToPhotoKeywordDto(PhotoKeyword photoKeyword) {
        return new PhotoKeywordDto(photoKeyword.getPhotoKeywordId(), photoKeyword.getKeyword(), photoKeyword.getPhotoPuzzleAction().getActionId());
    }
}
