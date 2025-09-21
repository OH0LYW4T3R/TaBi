package com.example.tabi.quest.actions.inputpuzzleaction.service;

import com.example.tabi.quest.actions.hint.service.HintService;
import com.example.tabi.quest.actions.hint.vo.HintRequest;
import com.example.tabi.quest.actions.inputpuzzleaction.entity.InputPuzzleAction;
import com.example.tabi.quest.actions.inputpuzzleaction.repository.InputPuzzleActionRepository;
import com.example.tabi.quest.actions.inputpuzzleaction.vo.InputPuzzleActionDto;
import com.example.tabi.quest.actions.inputpuzzleaction.vo.InputPuzzleActionRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InputPuzzleActionServiceJpaImpl implements InputPuzzleActionService {
    private final HintService hintService;
    private final InputPuzzleActionRepository inputPuzzleActionRepository;

    @Override
    @Transactional
    public InputPuzzleAction createInputPuzzleAction(InputPuzzleActionRequest inputPuzzleActionRequest, HintRequest hintRequest) {
        InputPuzzleAction inputPuzzleAction = new InputPuzzleAction();

        inputPuzzleAction.setAnswerString(inputPuzzleActionRequest.getAnswerString());

        inputPuzzleAction.setHint(hintService.createHint(hintRequest));

        inputPuzzleAction.setCharacterImageUrl(inputPuzzleActionRequest.getCharacterImageUrl());
        inputPuzzleAction.setQuestStep(inputPuzzleActionRequest.getQuestStep());

        inputPuzzleActionRepository.save(inputPuzzleAction);

        return inputPuzzleAction;
    }

    @Override
    public InputPuzzleAction retrieveInputPuzzleAction(Long inputPuzzleActionId) {
        Optional<InputPuzzleAction> inputPuzzleActionOptional = inputPuzzleActionRepository.findById(inputPuzzleActionId);

        return inputPuzzleActionOptional.orElse(null);
    }

    @Override
    @Transactional
    public InputPuzzleAction updateInputPuzzleAction(Long inputPuzzleActionId, InputPuzzleActionRequest inputPuzzleActionRequest, HintRequest hintRequest) {
        Optional<InputPuzzleAction> inputPuzzleActionOptional = inputPuzzleActionRepository.findById(inputPuzzleActionId);
        if (inputPuzzleActionOptional.isEmpty()) return null;

        InputPuzzleAction inputPuzzleAction = inputPuzzleActionOptional.get();

        if (inputPuzzleActionRequest.getAnswerString() != null)
            inputPuzzleAction.setAnswerString(inputPuzzleActionRequest.getAnswerString());

        if (inputPuzzleActionRequest.getCharacterImageUrl() != null)
            inputPuzzleAction.setCharacterImageUrl(inputPuzzleActionRequest.getCharacterImageUrl());

        if (inputPuzzleActionRequest.getQuestStep() != null)
            inputPuzzleAction.setQuestStep(inputPuzzleActionRequest.getQuestStep());

        if (hintRequest != null)
            inputPuzzleAction.setHint(hintService.createHint(hintRequest));

        inputPuzzleActionRepository.save(inputPuzzleAction);

        return inputPuzzleAction;
    }

    @Override
    @Transactional
    public void deleteInputPuzzleAction(Long id) {
        inputPuzzleActionRepository.deleteById(id);
    }
}
