package com.example.tabi.quest.actions.hint.service;

import com.example.tabi.quest.actions.hint.entity.Hint;
import com.example.tabi.quest.actions.hint.repository.HintRepository;
import com.example.tabi.quest.actions.hint.vo.HintRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HintServiceJpaImpl implements HintService {
    private static final Integer HINT_ONE_PRICE = 1;
    private static final Integer HINT_TWO_PRICE = 2;
    private static final Integer HINT_THREE_PRICE = 3;

    private final HintRepository hintRepository;

    @Override
    @Transactional
    public Hint createHint(HintRequest hintRequest) {
        Hint hint = new Hint();

        if (hintRequest.getHintOne() == null) {
            hint.setHintOne(null);
            hint.setHintOnePrice(null);
        } else {
            hint.setHintOne(hintRequest.getHintOne());
            hint.setHintOnePrice(HINT_ONE_PRICE);
        }

        if (hintRequest.getHintTwo() == null) {
            hint.setHintTwo(null);
            hint.setHintTwoPrice(null);
        } else {
            hint.setHintTwo(hintRequest.getHintTwo());
            hint.setHintTwoPrice(HINT_TWO_PRICE);
        }

        if (hintRequest.getHintThree() == null) {
            hint.setHintThree(null);
            hint.setHintThreePrice(null);
        } else {
            hint.setHintThree(hintRequest.getHintThree());
            hint.setHintThreePrice(HINT_THREE_PRICE);
        }

        hintRepository.save(hint);

        return hint;
    }

    @Override
    public Hint retrieveHint(Long hintId) {
        Optional<Hint> optionalHint = hintRepository.findById(hintId);

        return optionalHint.orElse(null);
    }

    @Override
    @Transactional
    public Hint updateHint(Long hintId, HintRequest hintRequest) {
        Optional<Hint> optional = hintRepository.findById(hintId);
        if (optional.isEmpty()) return null;

        Hint hint = optional.get();

        if (hintRequest.getHintOne() != null)
            hint.setHintOne(hintRequest.getHintOne());

        if (hintRequest.getHintTwo() != null)
            hint.setHintTwo(hintRequest.getHintTwo());

        if (hintRequest.getHintThree() != null)
            hint.setHintThree(hintRequest.getHintThree());

        hintRepository.save(hint);

        return hint;
    }
}
