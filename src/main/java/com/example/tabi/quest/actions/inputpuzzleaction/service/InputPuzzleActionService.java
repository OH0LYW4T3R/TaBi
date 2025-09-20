package com.example.tabi.quest.actions.inputpuzzleaction.service;

import com.example.tabi.quest.actions.hint.vo.HintRequest;
import com.example.tabi.quest.actions.inputpuzzleaction.vo.InputPuzzleActionDto;
import com.example.tabi.quest.actions.inputpuzzleaction.vo.InputPuzzleActionRequest;

public interface InputPuzzleActionService {
    InputPuzzleActionDto createInputPuzzleAction(InputPuzzleActionRequest inputPuzzleActionRequest, HintRequest hintRequest);
    InputPuzzleActionDto retrieveInputPuzzleAction(Long inputPuzzleActionId);
    InputPuzzleActionDto updateInputPuzzleAction(Long inputPuzzleActionId, InputPuzzleActionRequest inputPuzzleActionRequest, HintRequest hintRequest);
    void deleteInputPuzzleAction(Long inputPuzzleActionId);
}
