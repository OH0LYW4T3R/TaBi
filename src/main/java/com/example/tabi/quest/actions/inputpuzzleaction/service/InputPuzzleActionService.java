package com.example.tabi.quest.actions.inputpuzzleaction.service;

import com.example.tabi.quest.actions.hint.vo.HintRequest;
import com.example.tabi.quest.actions.inputpuzzleaction.entity.InputPuzzleAction;
import com.example.tabi.quest.actions.inputpuzzleaction.vo.InputPuzzleActionDto;
import com.example.tabi.quest.actions.inputpuzzleaction.vo.InputPuzzleActionRequest;

public interface InputPuzzleActionService {
    InputPuzzleAction createInputPuzzleAction(InputPuzzleActionRequest inputPuzzleActionRequest, HintRequest hintRequest);
    InputPuzzleAction retrieveInputPuzzleAction(Long inputPuzzleActionId);
    InputPuzzleAction updateInputPuzzleAction(Long inputPuzzleActionId, InputPuzzleActionRequest inputPuzzleActionRequest, HintRequest hintRequest);
    void deleteInputPuzzleAction(Long inputPuzzleActionId);
}
