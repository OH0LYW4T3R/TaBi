package com.example.tabi.quest.actions.photopuzzleaction.service;

import com.example.tabi.quest.actions.hint.vo.HintRequest;
import com.example.tabi.quest.actions.photopuzzleaction.vo.PhotoPuzzleActionDto;
import com.example.tabi.quest.actions.photopuzzleaction.vo.PhotoPuzzleActionRequest;
import com.example.tabi.quest.actions.stayingaction.vo.StayingActionDto;
import com.example.tabi.quest.actions.stayingaction.vo.StayingActionRequest;

public interface PhotoPuzzleActionService {
    PhotoPuzzleActionDto createPhotoPuzzleAction(PhotoPuzzleActionRequest photoPuzzleActionRequest, HintRequest hintRequest);
    PhotoPuzzleActionDto retrievePhotoPuzzleAction(Long photoPuzzleActionId);
    PhotoPuzzleActionDto updatePhotoPuzzleAction(Long photoPuzzleActionId, PhotoPuzzleActionRequest photoPuzzleActionRequest, HintRequest hintRequest);
    void deletePhotoPuzzleAction(Long photoPuzzleActionId);
}
