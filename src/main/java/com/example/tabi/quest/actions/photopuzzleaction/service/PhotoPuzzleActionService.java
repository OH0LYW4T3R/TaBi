package com.example.tabi.quest.actions.photopuzzleaction.service;

import com.example.tabi.quest.actions.hint.vo.HintRequest;
import com.example.tabi.quest.actions.photopuzzleaction.entity.PhotoPuzzleAction;
import com.example.tabi.quest.actions.photopuzzleaction.vo.PhotoPuzzleActionDto;
import com.example.tabi.quest.actions.photopuzzleaction.vo.PhotoPuzzleActionRequest;
import com.example.tabi.quest.actions.stayingaction.vo.StayingActionDto;
import com.example.tabi.quest.actions.stayingaction.vo.StayingActionRequest;

public interface PhotoPuzzleActionService {
    PhotoPuzzleAction createPhotoPuzzleAction(PhotoPuzzleActionRequest photoPuzzleActionRequest, HintRequest hintRequest);
    PhotoPuzzleAction retrievePhotoPuzzleAction(Long photoPuzzleActionId);
    PhotoPuzzleAction updatePhotoPuzzleAction(Long photoPuzzleActionId, PhotoPuzzleActionRequest photoPuzzleActionRequest, HintRequest hintRequest);
    void deletePhotoPuzzleAction(Long photoPuzzleActionId);
}
