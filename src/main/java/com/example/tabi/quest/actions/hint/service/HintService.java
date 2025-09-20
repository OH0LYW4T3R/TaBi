package com.example.tabi.quest.actions.hint.service;

import com.example.tabi.quest.actions.hint.entity.Hint;
import com.example.tabi.quest.actions.hint.vo.HintDto;
import com.example.tabi.quest.actions.hint.vo.HintRequest;

public interface HintService {
    Hint createHint(HintRequest hintRequest);
    Hint retrieveHint(Long hintId);
    Hint updateHint(Long hintId, HintRequest hintRequest);
}
