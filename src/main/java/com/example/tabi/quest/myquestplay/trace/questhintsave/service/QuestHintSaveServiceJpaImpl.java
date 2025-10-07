package com.example.tabi.quest.myquestplay.trace.questhintsave.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.repository.AppUserRepository;
import com.example.tabi.appuser.service.AppUserServiceJpaImpl;
import com.example.tabi.member.repository.MemberRepository;
import com.example.tabi.myinventory.entity.MyInventory;
import com.example.tabi.myinventory.repository.MyInventoryRepository;
import com.example.tabi.quest.actions.ActionType;
import com.example.tabi.quest.actions.action.entity.Action;
import com.example.tabi.quest.actions.action.repository.ActionRepository;
import com.example.tabi.quest.actions.hint.entity.Hint;
import com.example.tabi.quest.actions.inputpuzzleaction.entity.InputPuzzleAction;
import com.example.tabi.quest.actions.locationpuzzleaction.entity.LocationPuzzleAction;
import com.example.tabi.quest.actions.photopuzzleaction.entity.PhotoPuzzleAction;
import com.example.tabi.quest.myquestplay.entity.MyQuestPlay;
import com.example.tabi.quest.myquestplay.repository.MyQuestPlayRepository;
import com.example.tabi.quest.myquestplay.trace.questcurrentpoint.entity.QuestCurrentPoint;
import com.example.tabi.quest.myquestplay.trace.questcurrentpoint.repository.QuestCurrentPointRepository;
import com.example.tabi.quest.myquestplay.trace.questhintsave.entity.QuestHintSave;
import com.example.tabi.quest.myquestplay.trace.questhintsave.repository.QuestHintSaveRepository;
import com.example.tabi.quest.myquestplay.trace.questhintsave.vo.HintContentsDto;
import com.example.tabi.quest.myquestplay.trace.questhintsave.vo.HintSetDto;
import com.example.tabi.quest.myquestplay.trace.questhintsave.vo.QuestHintSaveRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestHintSaveServiceJpaImpl implements QuestHintSaveService {
    private final ActionRepository actionRepository;
    private final QuestCurrentPointRepository questCurrentPointRepository;
    private final QuestHintSaveRepository questHintSaveRepository;
    private final MyQuestPlayRepository myQuestPlayRepository;
    private final AppUserRepository appUserRepository;
    private final MemberRepository memberRepository;
    private final MyInventoryRepository myInventoryRepository;

    @Override
    @Transactional
    public HintContentsDto purchaseHint(Authentication authentication, QuestHintSaveRequest questHintSaveRequest) {
        HintContentsDto hintContentsDto = new HintContentsDto();
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty()) {
            hintContentsDto.setErrorMessage("AppUser Not Found");
            return hintContentsDto;
        }

        AppUser appUser = optionalAppUser.get();

        Optional<QuestCurrentPoint> questCurrentPointOptional = questCurrentPointRepository.findById(questHintSaveRequest.getQuestCurrentPointId());

        if (questCurrentPointOptional.isEmpty()) {
            hintContentsDto.setErrorMessage("QuestCurrentPoint Id not found");
            return hintContentsDto;
        }

        if (questHintSaveRequest.getQuestCurrentPointId() == null || questHintSaveRequest.getPurchaseHintIndex() < 1 || questHintSaveRequest.getPurchaseHintIndex() > 3) {
                hintContentsDto.setErrorMessage("Unsupported hint index");
                return hintContentsDto;
        }

        QuestCurrentPoint questCurrentPoint = questCurrentPointOptional.get();
        Long actionId = questCurrentPoint.getCurrentActionId();
        Optional<Action> actionOptional = actionRepository.findById(actionId);
        if (actionOptional.isEmpty()) {
            hintContentsDto.setErrorMessage("Action not found");
            return hintContentsDto;
        }

        Action action = actionOptional.get();
        Long hintId;
        String[] hintSet = new String[3];
        ActionType currentActionType;

        if (action instanceof InputPuzzleAction inputPuzzleAction) {
            currentActionType = ActionType.INPUT_PUZZLE;

            Hint hint = inputPuzzleAction.getHint();
            hintId = hint.getHintId();

            hintSet[0] = hint.getHintOne();
            hintSet[1] = hint.getHintTwo();
            hintSet[2] = hint.getHintThree();
        } else if (action instanceof PhotoPuzzleAction photoPuzzleAction) {
            currentActionType = ActionType.PHOTO_PUZZLE;

            Hint hint = photoPuzzleAction.getHint();
            hintId = hint.getHintId();

            hintSet[0] = hint.getHintOne();
            hintSet[1] = hint.getHintTwo();
            hintSet[2] = hint.getHintThree();
        } else if (action instanceof LocationPuzzleAction locationPuzzleAction) {
            currentActionType = ActionType.LOCATION_PUZZLE;

            Hint hint = locationPuzzleAction.getHint();
            hintId = hint.getHintId();

            hintSet[0] = hint.getHintOne();
            hintSet[1] = hint.getHintTwo();
            hintSet[2] = hint.getHintThree();
        } else {
            hintContentsDto.setErrorMessage("Action not found");
            return hintContentsDto;
        }

        MyQuestPlay myQuestPlay = questCurrentPoint.getMyQuestPlay();
        MyInventory myInventory = appUser.getMyInventory();
        Integer myCoin = myInventory.getCoins();

        List<QuestHintSave> checkQuestHintSaves = myQuestPlay.getQuestHintSaves();
        Optional<QuestHintSave> existingOpt = checkQuestHintSaves.stream().filter(h -> Objects.equals(h.getActionId(), actionId)).findFirst();

        // 퍼즐인 경우에는 액션 조회후 다음으로 넘어가지 않으므로 그대로 사용 가능
        if (existingOpt.isPresent()) {
            QuestHintSave questHintSave = existingOpt.get();
            int idx = questHintSaveRequest.getPurchaseHintIndex();

            switch (idx) {
                case 1 -> {
                    if (!questHintSave.isHintOneLocked()) {
                        hintContentsDto.setErrorMessage("Hint #1 already purchased");
                        return hintContentsDto;
                    }
                    // 가격: 1코인
                    if (!requireCoins(myInventory, 1, hintContentsDto)) return hintContentsDto;

                    questHintSave.setHintOneLocked(false);
                    questHintSaveRepository.save(questHintSave);

                    return HintContentsDto.questHintSaveToHintContentsDto(questHintSave, hintSet[0]);
                }
                case 2 -> {
                    if (questHintSave.isHintOneLocked()) {
                        hintContentsDto.setErrorMessage("You must buy hint #1 first");
                        return hintContentsDto;
                    }
                    if (!questHintSave.isHintTwoLocked()) {
                        hintContentsDto.setErrorMessage("Hint #2 already purchased");
                        return hintContentsDto;
                    }
                    // 가격: 2코인
                    if (!requireCoins(myInventory, 2, hintContentsDto)) return hintContentsDto;

                    questHintSave.setHintTwoLocked(false);
                    questHintSaveRepository.save(questHintSave);
                    return HintContentsDto.questHintSaveToHintContentsDto(questHintSave, hintSet[1]);
                }
                case 3 -> {
                    if (questHintSave.isHintOneLocked() || questHintSave.isHintTwoLocked()) {
                        hintContentsDto.setErrorMessage("You must buy hints #1 and #2 first");
                        return hintContentsDto;
                    }
                    if (!questHintSave.isHintThreeLocked()) {
                        hintContentsDto.setErrorMessage("Hint #3 already purchased");
                        return hintContentsDto;
                    }
                    // 가격: 3코인
                    if (!requireCoins(myInventory, 3, hintContentsDto)) return hintContentsDto;

                    questHintSave.setHintThreeLocked(false);
                    questHintSaveRepository.save(questHintSave);
                    return HintContentsDto.questHintSaveToHintContentsDto(questHintSave, hintSet[2]);
                }
                default -> {
                    hintContentsDto.setErrorMessage("Unsupported hint index");
                    return hintContentsDto;
                }
            }
        } else {
            // 최초 구매는 1번만 허용
            int idx = questHintSaveRequest.getPurchaseHintIndex();
            if (idx == 2 || idx == 3) {
                hintContentsDto.setErrorMessage("You must buy it from number one");
                return hintContentsDto;
            }
            // 가격: 1코인
            if (!requireCoins(myInventory, 1, hintContentsDto)) return hintContentsDto;

            QuestHintSave questHintSave = new QuestHintSave();
            questHintSave.setQuestRunningLocationId(questCurrentPoint.getCurrentQuestRunningLocationId());
            questHintSave.setQuestRunningLocationNumber(questCurrentPoint.getCurrentQuestRunningLocationIndex());
            questHintSave.setActionId(actionId);
            questHintSave.setActionNumber(questCurrentPoint.getCurrentActionIndex());
            questHintSave.setActionType(currentActionType);
            questHintSave.setHintId(hintId);
            questHintSave.setHintOneLocked(false);
            questHintSave.setHintTwoLocked(true);
            questHintSave.setHintThreeLocked(true);
            questHintSave.setMyQuestPlay(myQuestPlay);

            myQuestPlay.getQuestHintSaves().add(questHintSaveRepository.save(questHintSave));
            myQuestPlayRepository.save(myQuestPlay);

            return HintContentsDto.questHintSaveToHintContentsDto(questHintSave, hintSet[0]);
        }
    }

    @Override
    public HintSetDto readMyHint(Long questHintSaveId) {
        Optional<QuestHintSave> questHintSaveOptional = questHintSaveRepository.findById(questHintSaveId);
        if (questHintSaveOptional.isEmpty()) return null;

        QuestHintSave questHintSave = questHintSaveOptional.get();
        Optional<Action> actionOptional = actionRepository.findById(questHintSave.getActionId());
        if (actionOptional.isEmpty()) return null;

        Action action = actionOptional.get();
        if (action instanceof InputPuzzleAction inputPuzzleAction) {
            Hint hint = inputPuzzleAction.getHint();
            return HintSetDto.createHintSetDto(questHintSave, hint);
        } else if (action instanceof PhotoPuzzleAction photoPuzzleAction) {
            Hint hint = photoPuzzleAction.getHint();
            return HintSetDto.createHintSetDto(questHintSave, hint);
        } else if (action instanceof LocationPuzzleAction locationPuzzleAction) {
            Hint hint = locationPuzzleAction.getHint();
            return HintSetDto.createHintSetDto(questHintSave, hint);
        } else
            return null;
    }

    private boolean requireCoins(MyInventory myInventory, int cost, HintContentsDto hintContentsDto) {
        Integer coins = myInventory.getCoins();

        if (coins == null) coins = 0;

        if (coins < cost) {
            hintContentsDto.setErrorMessage("Not enough coins");
            return false;
        }

        myInventory.setCoins(coins - cost);
        myInventoryRepository.save(myInventory);

        return true;
    }
}
