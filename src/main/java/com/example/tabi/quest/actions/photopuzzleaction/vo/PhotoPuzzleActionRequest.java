package com.example.tabi.quest.actions.photopuzzleaction.vo;

import com.example.tabi.quest.actions.photopuzzleaction.entity.PhotoKeyword;
import com.example.tabi.quest.queststep.entity.QuestStep;
import lombok.Data;

import java.util.List;

@Data
public class PhotoPuzzleActionRequest {
    private List<PhotoKeywordRequest> photoKeywordRequests;
    private String characterImageUrl;
    private QuestStep questStep;
}
