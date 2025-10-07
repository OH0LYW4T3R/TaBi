package com.example.tabi.quest.myquestplay.trace.questhintsave.service;

import com.example.tabi.quest.myquestplay.trace.questhintsave.vo.HintContentsDto;
import com.example.tabi.quest.myquestplay.trace.questhintsave.vo.HintSetDto;
import com.example.tabi.quest.myquestplay.trace.questhintsave.vo.QuestHintSaveRequest;
import org.springframework.security.core.Authentication;

public interface QuestHintSaveService {
    // 첫 생성은 구매로 부터 시작
    // MyQuestPlay의 List<QuestHintSave>의 안에서 QuestHingSave의 hintId는 리스트 객체중 유일한 값임 밖에 없음 (하나만 존재하게 해야함.)
    HintContentsDto purchaseHint(Authentication authentication, QuestHintSaveRequest questHintSaveRequest);
    HintSetDto readMyHint(Long questHintSaveId);
}
