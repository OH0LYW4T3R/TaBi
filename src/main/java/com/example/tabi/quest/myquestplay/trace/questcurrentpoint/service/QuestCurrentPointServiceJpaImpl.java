package com.example.tabi.quest.myquestplay.trace.questcurrentpoint.service;

import com.example.tabi.quest.actions.ActionType;
import com.example.tabi.quest.actions.action.entity.Action;
import com.example.tabi.quest.actions.hint.entity.Hint;
import com.example.tabi.quest.actions.inputpuzzleaction.entity.InputPuzzleAction;
import com.example.tabi.quest.actions.locationpuzzleaction.entity.LocationPuzzleAction;
import com.example.tabi.quest.actions.photopuzzleaction.entity.PhotoKeyword;
import com.example.tabi.quest.actions.photopuzzleaction.entity.PhotoPuzzleAction;
import com.example.tabi.quest.actions.stayingaction.entity.StayingAction;
import com.example.tabi.quest.actions.talkingaction.entity.TalkingAction;
import com.example.tabi.quest.actions.talkingaction.vo.TalkingActionDto;
import com.example.tabi.quest.actions.walkingaction.entity.WalkingAction;
import com.example.tabi.quest.myquestplay.entity.MyQuestPlay;
import com.example.tabi.quest.myquestplay.repository.MyQuestPlayRepository;
import com.example.tabi.quest.myquestplay.trace.questcurrentpoint.entity.QuestCurrentPoint;
import com.example.tabi.quest.myquestplay.trace.questcurrentpoint.repository.QuestCurrentPointRepository;
import com.example.tabi.quest.myquestplay.trace.questcurrentpoint.vo.*;
import com.example.tabi.quest.myquestplay.trace.questhintsave.entity.QuestHintSave;
import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.quest.questrunninglocation.entity.QuestRunningLocation;
import com.example.tabi.quest.queststartlocation.entity.QuestStartLocation;
import com.example.tabi.quest.queststep.entity.QuestStep;
import com.example.tabi.util.GeoUtil;
import com.example.tabi.util.PlayStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestCurrentPointServiceJpaImpl implements QuestCurrentPointService {
    private static final Double ACCEPT_BASE_RADIUS_KM = 0.015;
    private final MyQuestPlayRepository myQuestPlayRepository;
    private final QuestCurrentPointRepository questCurrentPointRepository;
    private final ImageProcessingService imageProcessingService;

    @Override
    @Transactional
    public QuestCurrentPoint createQuestCurrentPoint(MyQuestPlay myQuestPlay) {
        QuestPost questPost = myQuestPlay.getQuestPost();

        List<QuestRunningLocation> questRunningLocations = questPost.getQuest().getQuestRunningLocations();
        questRunningLocations.sort(Comparator.comparingInt(QuestRunningLocation::getSequence));

        QuestRunningLocation startQuestRunningLocation = questRunningLocations.get(0);
        QuestRunningLocation endQuestRunningLocation = questRunningLocations.get(questRunningLocations.size() - 1);

        List<QuestStep> questSteps = questPost.getQuest().getQuestRunningLocations().get(0).getQuestIndicating().getQuestSteps();
        questSteps.sort(Comparator.comparingInt(QuestStep::getSequence));
        QuestStep startQuestStep = questSteps.get(0);
        QuestStep endQuestStep = questSteps.get(questSteps.size() - 1);

        QuestCurrentPoint questCurrentPoint = new QuestCurrentPoint();

        questCurrentPoint.setCurrentQuestRunningLocationId(startQuestRunningLocation.getQuestRunningLocationId());
        questCurrentPoint.setCurrentQuestRunningLocationIndex(0);

        questCurrentPoint.setCurrentActionId(startQuestStep.getAction().getActionId());
        questCurrentPoint.setCurrentActionIndex(0);

        questCurrentPoint.setEndQuestRunningLocationId(endQuestRunningLocation.getQuestRunningLocationId());
        questCurrentPoint.setEndQuestRunningLocationIndex(endQuestRunningLocation.getSequence() - 1); // 인덱스가 0based 이므로

        questCurrentPoint.setEndActionId(endQuestStep.getAction().getActionId());
        questCurrentPoint.setEndActionIndex(endQuestStep.getSequence() - 1); // 인덱스가 0based 이므로

        questCurrentPoint.setMyQuestPlay(myQuestPlay);

        questCurrentPointRepository.save(questCurrentPoint);

        return questCurrentPoint;
    }

    @Override
    @Transactional
    public QuestCurrentPointDto retrieveQuestCurrentPointDetail(Long myQuestPlayId) {
        Optional<MyQuestPlay> myQuestPlayOptional = myQuestPlayRepository.findById(myQuestPlayId);
        if (myQuestPlayOptional.isEmpty()) return null;

        MyQuestPlay myQuestPlay = myQuestPlayOptional.get();
        if (myQuestPlay.getPlayStatus() != PlayStatus.PLAYING) return null;

        QuestCurrentPoint questCurrentPoint = myQuestPlay.getQuestCurrentPoint();
        Integer currentQuestRunningLocationIndex = questCurrentPoint.getCurrentQuestRunningLocationIndex();
        Integer currentActionIndex = questCurrentPoint.getCurrentActionIndex();

        System.out.println(currentActionIndex);

        QuestRunningLocation currentQuestRunningLocation = myQuestPlay.getQuestPost().getQuest().getQuestRunningLocations().get(currentQuestRunningLocationIndex);
        Action currentAction = currentQuestRunningLocation.getQuestIndicating().getQuestSteps().get(currentActionIndex).getAction();
        List<QuestHintSave> questHintSaves = myQuestPlay.getQuestHintSaves();

        QuestCurrentPointDto questCurrentPointDto;

        if (currentAction instanceof WalkingAction walkingAction) {
            questCurrentPointDto = QuestCurrentPointDto.questCurrentPointToQuestCurrentPointDto(questCurrentPoint, walkingAction, null);

        } else if (currentAction instanceof TalkingAction talkingAction) {
            questCurrentPointDto = QuestCurrentPointDto.questCurrentPointToQuestCurrentPointDto(questCurrentPoint, talkingAction, null);

        } else if (currentAction instanceof StayingAction stayingAction) {
            questCurrentPointDto = QuestCurrentPointDto.questCurrentPointToQuestCurrentPointDto(questCurrentPoint, stayingAction, null);

        } else if (currentAction instanceof PhotoPuzzleAction photoPuzzleAction) {
            Hint hint = photoPuzzleAction.getHint();
            QuestHintSave findQuestHintSave = questHintSaves.stream().filter(h -> Objects.equals(h.getHintId(), hint.getHintId())).findFirst().orElse(null);

            return QuestCurrentPointDto.questCurrentPointToQuestCurrentPointDto(questCurrentPoint, photoPuzzleAction, findQuestHintSave);

        } else if (currentAction instanceof LocationPuzzleAction locationPuzzleAction) {
            Hint hint = locationPuzzleAction.getHint();
            QuestHintSave findQuestHintSave = questHintSaves.stream().filter(h -> Objects.equals(h.getHintId(), hint.getHintId())).findFirst().orElse(null);

            return QuestCurrentPointDto.questCurrentPointToQuestCurrentPointDto(questCurrentPoint, locationPuzzleAction, findQuestHintSave);

        } else if (currentAction instanceof InputPuzzleAction inputPuzzleAction) {
            Hint hint = inputPuzzleAction.getHint();
            QuestHintSave findQuestHintSave = questHintSaves.stream().filter(h -> Objects.equals(h.getHintId(), hint.getHintId())).findFirst().orElse(null);

            return QuestCurrentPointDto.questCurrentPointToQuestCurrentPointDto(questCurrentPoint, inputPuzzleAction, findQuestHintSave);

        } else {
            throw new IllegalArgumentException("Unsupported Action subtype: " + currentAction.getClass());
        }

        // 퍼즐을 제외한 나머지 액션들은 다음으로 위치
        if (currentActionIndex + 1 <= currentQuestRunningLocation.getQuestIndicating().getQuestSteps().size() - 1) {
            Integer nextActionIndex = questCurrentPoint.getCurrentActionIndex() + 1;
            questCurrentPoint.setCurrentActionIndex(nextActionIndex);
            currentQuestRunningLocation.getQuestIndicating().getQuestSteps().sort(Comparator.comparingInt(QuestStep::getSequence));
            questCurrentPoint.setCurrentActionId(currentQuestRunningLocation.getQuestIndicating().getQuestSteps().get(nextActionIndex).getAction().getActionId());

            questCurrentPointRepository.save(questCurrentPoint);
        }

        return questCurrentPointDto;
    }

    @Override
    public QuestNextLocationDto retrieveQuestCurrentLocationInfo(Long myQuestPlayId) {
        QuestNextLocationDto questNextLocationForErrorDto = new QuestNextLocationDto();

        Optional<MyQuestPlay> myQuestPlayOptional = myQuestPlayRepository.findById(myQuestPlayId);
        if (myQuestPlayOptional.isEmpty()) {
            questNextLocationForErrorDto.setErrorMessage("My Quest Play Id is not found");
            return questNextLocationForErrorDto;
        }

        MyQuestPlay myQuestPlay = myQuestPlayOptional.get();
//        if (myQuestPlay.getPlayStatus() != PlayStatus.PLAYING) {
//            questNextLocationForErrorDto.setErrorMessage("Current Quest Play Status is not playing");
//            return questNextLocationForErrorDto;
//        }

        QuestCurrentPoint questCurrentPoint = myQuestPlay.getQuestCurrentPoint();
        Integer currentQuestRunningLocationIndex = questCurrentPoint.getCurrentQuestRunningLocationIndex();
        QuestRunningLocation currentQuestRunningLocation = myQuestPlay.getQuestPost().getQuest().getQuestRunningLocations().get(currentQuestRunningLocationIndex);

        return QuestNextLocationDto.createQuestNextLocationDto(questCurrentPoint, currentQuestRunningLocation);
    }

    @Override
    public QuestNextLocationDto retrieveQuestNextLocationSetting(Long myQuestPlayId, QuestUserLocationRequest questUserLocationRequest) {
        QuestNextLocationDto questNextLocationForErrorDto = new QuestNextLocationDto();

        Optional<MyQuestPlay> myQuestPlayOptional = myQuestPlayRepository.findById(myQuestPlayId);
        if (myQuestPlayOptional.isEmpty()) {
            questNextLocationForErrorDto.setErrorMessage("My Quest Play Id is not found");
            return questNextLocationForErrorDto;
        }

        MyQuestPlay myQuestPlay = myQuestPlayOptional.get();
        if (myQuestPlay.getPlayStatus() != PlayStatus.PLAYING) {
            questNextLocationForErrorDto.setErrorMessage("Current Quest Play Status is not playing");
            return questNextLocationForErrorDto;
        }

        QuestCurrentPoint questCurrentPoint = myQuestPlay.getQuestCurrentPoint();
        Integer currentQuestRunningLocationIndex = questCurrentPoint.getCurrentQuestRunningLocationIndex();
        Integer currentActionIndex = questCurrentPoint.getCurrentActionIndex();
        Integer endActionIndex = questCurrentPoint.getEndActionIndex();

        if (!currentActionIndex.equals(endActionIndex)) {
            questNextLocationForErrorDto.setErrorMessage("The action did not reach the end");
            return questNextLocationForErrorDto;
        }

        List<QuestRunningLocation> currentQuestRunningLocations = myQuestPlay.getQuestPost().getQuest().getQuestRunningLocations();
        currentQuestRunningLocations.sort(Comparator.comparingInt(QuestRunningLocation::getSequence));

        if (currentQuestRunningLocationIndex + 1 <= currentQuestRunningLocations.size() - 1) { // 실행장소가 마지막을 벗어나지 않았다면
            Integer nextQuestRunningLocationIndex = questCurrentPoint.getCurrentQuestRunningLocationIndex() + 1;
            QuestRunningLocation nextQuestRunningLocation = currentQuestRunningLocations.get(nextQuestRunningLocationIndex);

            // 있어야함.
            if (!GeoUtil.isWithinRadius(nextQuestRunningLocation.getLatitude(), nextQuestRunningLocation.getLongitude(), questUserLocationRequest.getLatitude(), questUserLocationRequest.getLongitude(), ACCEPT_BASE_RADIUS_KM)) {
                questNextLocationForErrorDto.setErrorMessage("Does not exist in the scope of the quest execution location.");
                return questNextLocationForErrorDto;
            }

            questCurrentPoint.setCurrentQuestRunningLocationIndex(nextQuestRunningLocationIndex);
            questCurrentPoint.setCurrentQuestRunningLocationId(nextQuestRunningLocation.getQuestRunningLocationId());

            // 다음 위치 넘어갔으므로 첫번째 액션으로 셋팅
            List<QuestStep> nextRunningLocationQuestSteps = currentQuestRunningLocations.get(nextQuestRunningLocationIndex).getQuestIndicating().getQuestSteps();
            nextRunningLocationQuestSteps.sort(Comparator.comparingInt(QuestStep::getSequence));

            questCurrentPoint.setCurrentActionIndex(0);
            questCurrentPoint.setCurrentActionId(nextRunningLocationQuestSteps.get(0).getAction().getActionId());

            // 끝 인덱스도 다음 위치의 액션리스트의 마지막으로 갱신
            int endIndex = nextRunningLocationQuestSteps.size() - 1;
            questCurrentPoint.setEndActionIndex(endIndex);
            questCurrentPoint.setEndActionId(nextRunningLocationQuestSteps.get(endIndex).getAction().getActionId());

            questCurrentPointRepository.save(questCurrentPoint);

            return QuestNextLocationDto.createQuestNextLocationDto(questCurrentPoint, currentQuestRunningLocations.get(nextQuestRunningLocationIndex));
        } else {
            questNextLocationForErrorDto.setErrorMessage("You have already reached your final position");
            questNextLocationForErrorDto.setEndLocation(true);
            return questNextLocationForErrorDto;
        }
    }

    @Override
    public QuestNextLocationDto retrieveQuestNextLocationInfo(Long myQuestPlayId) {
        // 다음 위치 요청후 원래 로컬 데이터로 저장되어 있던것을 삭제하고 해당 위치로 업데이트
        // 해당 위치에서 멀어지면 pending
        QuestNextLocationDto questNextLocationForErrorDto = new QuestNextLocationDto();

        Optional<MyQuestPlay> myQuestPlayOptional = myQuestPlayRepository.findById(myQuestPlayId);
        if (myQuestPlayOptional.isEmpty()) {
            questNextLocationForErrorDto.setErrorMessage("My Quest Play Id is not found");
            return questNextLocationForErrorDto;
        }

        MyQuestPlay myQuestPlay = myQuestPlayOptional.get();
        if (myQuestPlay.getPlayStatus() != PlayStatus.PLAYING) {
            questNextLocationForErrorDto.setErrorMessage("Current Quest Play Status is not playing");
            return questNextLocationForErrorDto;
        }

        QuestCurrentPoint questCurrentPoint = myQuestPlay.getQuestCurrentPoint();
        Integer currentQuestRunningLocationIndex = questCurrentPoint.getCurrentQuestRunningLocationIndex();
        Integer currentActionIndex = questCurrentPoint.getCurrentActionIndex();
        Integer endActionIndex = questCurrentPoint.getEndActionIndex();

        if (!currentActionIndex.equals(endActionIndex)) {
            questNextLocationForErrorDto.setErrorMessage("The action did not reach the end");
            return questNextLocationForErrorDto;
        }

        List<QuestRunningLocation> currentQuestRunningLocations = myQuestPlay.getQuestPost().getQuest().getQuestRunningLocations();
        if (currentQuestRunningLocationIndex + 1 <= currentQuestRunningLocations.size() - 1) {
            QuestRunningLocation nextQuestRunningLocation = currentQuestRunningLocations.get(currentQuestRunningLocationIndex + 1);
            return QuestNextLocationDto.createQuestNextLocationDto(questCurrentPoint, nextQuestRunningLocation);
        } else {
            questNextLocationForErrorDto.setErrorMessage("You have already reached your final position");
            questNextLocationForErrorDto.setEndLocation(true);
            return questNextLocationForErrorDto;
        }
    }

    @Override
    @Transactional
    public QuestAnswerCheckDto checkAnswer(Long myQuestPlayId, QuestCurrentPointAnswerRequest questCurrentPointAnswerRequest) {
        // 퍼즐에 대해서 정답인 경우 actionIndex 이동
        QuestAnswerCheckDto questAnswerCheckDto = new QuestAnswerCheckDto();

        Optional<MyQuestPlay> myQuestPlayOptional = myQuestPlayRepository.findById(myQuestPlayId);
        if (myQuestPlayOptional.isEmpty()) {
            questAnswerCheckDto.setErrorMessage("My Quest Play Id is not found");
            return questAnswerCheckDto;
        }

        MyQuestPlay myQuestPlay = myQuestPlayOptional.get();
        if (myQuestPlay.getPlayStatus() != PlayStatus.PLAYING) {
            questAnswerCheckDto.setErrorMessage("Current Quest Play Status is not playing");
            return questAnswerCheckDto;
        }

        QuestCurrentPoint questCurrentPoint = myQuestPlay.getQuestCurrentPoint();
        Integer currentQuestRunningLocationIndex = questCurrentPoint.getCurrentQuestRunningLocationIndex();
        Integer currentActionIndex = questCurrentPoint.getCurrentActionIndex();

        QuestRunningLocation currentQuestRunningLocation = myQuestPlay.getQuestPost().getQuest().getQuestRunningLocations().get(currentQuestRunningLocationIndex);
        Action currentAction = currentQuestRunningLocation.getQuestIndicating().getQuestSteps().get(currentActionIndex).getAction();

        if (currentAction instanceof PhotoPuzzleAction photoPuzzleAction) {
            if (!questCurrentPointAnswerRequest.getActionType().equals(ActionType.PHOTO_PUZZLE)) {
                questAnswerCheckDto.setErrorMessage("[Server Error] - The request type and the type stored on the server are different.");
                System.out.println("Server : Photo Puzzle" + " / " + "Request : " + questCurrentPointAnswerRequest.getActionType().toString());
                return questAnswerCheckDto;
            }

            if (keywordMatchingAboutImage(questCurrentPointAnswerRequest.getSubmissionImage(), photoPuzzleAction.getPhotoKeywords())) questAnswerCheckDto.setAnswered(true);
            else {
                questAnswerCheckDto.setAnswered(false);
                return questAnswerCheckDto;
            }

        } else if (currentAction instanceof LocationPuzzleAction locationPuzzleAction) {
            if (!questCurrentPointAnswerRequest.getActionType().equals(ActionType.LOCATION_PUZZLE)) {
                questAnswerCheckDto.setErrorMessage("[Server Error] - The request type and the type stored on the server are different.");
                System.out.println("Server : Location Puzzle" + " / " + "Request : " + questCurrentPointAnswerRequest.getActionType().toString());
                return questAnswerCheckDto;
            }

            if (GeoUtil.isWithinRadius(locationPuzzleAction.getLatitude(), locationPuzzleAction.getLongitude(), questCurrentPointAnswerRequest.getLatitude(),  questCurrentPointAnswerRequest.getLongitude(), ACCEPT_BASE_RADIUS_KM))
                questAnswerCheckDto.setAnswered(true);
            else {
                questAnswerCheckDto.setAnswered(false);
                return questAnswerCheckDto;
            }

        } else if (currentAction instanceof InputPuzzleAction inputPuzzleAction) {
            if (!questCurrentPointAnswerRequest.getActionType().equals(ActionType.INPUT_PUZZLE)) {
                questAnswerCheckDto.setErrorMessage("[Server Error] - The request type and the type stored on the server are different.");
                System.out.println("Server : Input Puzzle" + " / " + "Request : " + questCurrentPointAnswerRequest.getActionType().toString());
                return questAnswerCheckDto;
            }

            if (inputPuzzleAction.getAnswerString().equals(questCurrentPointAnswerRequest.getSubmissionAnswerString())) questAnswerCheckDto.setAnswered(true);
            else {
                questAnswerCheckDto.setAnswered(false);
                return questAnswerCheckDto;
            }

        } else {
            questAnswerCheckDto.setErrorMessage("Not puzzle action or not supported action.");
            return questAnswerCheckDto;
        }

        if (currentActionIndex + 1 <= currentQuestRunningLocation.getQuestIndicating().getQuestSteps().size() - 1) {
            Integer nextActionIndex = questCurrentPoint.getCurrentActionIndex() + 1;
            questCurrentPoint.setCurrentActionIndex(nextActionIndex);
            questCurrentPoint.setCurrentActionId(currentQuestRunningLocation.getQuestIndicating().getQuestSteps().get(nextActionIndex).getAction().getActionId());

            questCurrentPointRepository.save(questCurrentPoint);
        }

        return questAnswerCheckDto;
    }

    @Override
    public QuestMostRecentTalkingActionDto retrieveMostRecentTalkingAction(Long myQuestPlayId) {
        QuestMostRecentTalkingActionDto questMostRecentTalkingActionForErrorDto = new QuestMostRecentTalkingActionDto();

        Optional<MyQuestPlay> myQuestPlayOptional = myQuestPlayRepository.findById(myQuestPlayId);
        if (myQuestPlayOptional.isEmpty()) {
            questMostRecentTalkingActionForErrorDto.setErrorMessage("My Quest Play Id is not found");
            return questMostRecentTalkingActionForErrorDto;
        }

        MyQuestPlay myQuestPlay = myQuestPlayOptional.get();
        if (myQuestPlay.getPlayStatus() != PlayStatus.PLAYING) {
            questMostRecentTalkingActionForErrorDto.setErrorMessage("Current Quest Play Status is not playing");
            return questMostRecentTalkingActionForErrorDto;
        }

        QuestCurrentPoint questCurrentPoint = myQuestPlay.getQuestCurrentPoint();
        Integer currentQuestRunningLocationIndex = questCurrentPoint.getCurrentQuestRunningLocationIndex();
        Integer currentActionIndex = questCurrentPoint.getCurrentActionIndex();

        QuestRunningLocation currentQuestRunningLocation = myQuestPlay.getQuestPost().getQuest().getQuestRunningLocations().get(currentQuestRunningLocationIndex);
        List<QuestStep> questSteps = currentQuestRunningLocation.getQuestIndicating().getQuestSteps();
        questSteps.sort(Comparator.comparingInt(QuestStep::getSequence));

        for (int i = currentActionIndex; i >= 0; i--) {
            Action action = questSteps.get(i).getAction();

            if (action instanceof TalkingAction talkingAction) {
                QuestMostRecentTalkingActionDto questMostRecentTalkingActionDto = new QuestMostRecentTalkingActionDto();
                questMostRecentTalkingActionDto.setTalkingActionDto(talkingAction.actionToActionDto());
                return questMostRecentTalkingActionDto;
            }
        }

        questMostRecentTalkingActionForErrorDto.setErrorMessage("Recent Talking Not Found");
        return questMostRecentTalkingActionForErrorDto;
    }

    @Override
    @Transactional
    public void deleteQuestCurrentPointById(Long questCurrentPointId) {
        questCurrentPointRepository.deleteById(questCurrentPointId);
    }

    private Boolean keywordMatchingAboutImage(MultipartFile imageFile, List<PhotoKeyword> photoKeywords) {
        return imageProcessingService.checkKeywordsInImage(imageFile, photoKeywords);
    }
}
