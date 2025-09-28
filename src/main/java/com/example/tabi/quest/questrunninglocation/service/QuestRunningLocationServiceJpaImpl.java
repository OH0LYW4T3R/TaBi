package com.example.tabi.quest.questrunninglocation.service;

import com.example.tabi.quest.quest.entity.Quest;
import com.example.tabi.quest.quest.repository.QuestRepository;
import com.example.tabi.quest.questindicating.service.QuestIndicatingService;
import com.example.tabi.quest.questrunninglocation.entity.QuestRunningLocation;
import com.example.tabi.quest.questrunninglocation.repository.QuestRunningLocationRepository;
import com.example.tabi.quest.questrunninglocation.vo.QuestRunningLocationDto;
import com.example.tabi.quest.questrunninglocation.vo.QuestRunningLocationRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestRunningLocationServiceJpaImpl implements QuestRunningLocationService {
    private final QuestRepository questRepository;
    private final QuestRunningLocationRepository questRunningLocationRepository;
    private final QuestIndicatingService questIndicatingService;

    @Override
    @Transactional
    public List<QuestRunningLocationDto> saveQuestRunningLocation(Long questId, List<QuestRunningLocationRequest> questRunningLocationRequests) {
        if (questRunningLocationRequests == null || questRunningLocationRequests.isEmpty())
            return null;

        Optional<Quest> questOptional = questRepository.findById(questId);

        if (questOptional.isEmpty())
            return null;

        List<QuestRunningLocationDto> questRunningLocationDtos = new ArrayList<>();
        Quest quest = questOptional.get();

        if (quest.getQuestRunningLocations().isEmpty()) {
            // 처음 QuestRunningLocation을 만들때
            for (QuestRunningLocationRequest questRunningLocationRequest : questRunningLocationRequests) {
                QuestRunningLocation questRunningLocation = new QuestRunningLocation();

                questRunningLocation.setSequence(questRunningLocationRequest.getSequence());
                questRunningLocation.setLocationName(questRunningLocationRequest.getLocationName());
                questRunningLocation.setDetailLocation(questRunningLocationRequest.getDetailLocation());

                questRunningLocation.setLatitude(questRunningLocationRequest.getLatitude());
                questRunningLocation.setLongitude(questRunningLocationRequest.getLongitude());
                questRunningLocation.setAltitude(questRunningLocationRequest.getAltitude());

                questRunningLocation.setQuest(quest);

                questRunningLocationRepository.save(questRunningLocation);

                questRunningLocation.setQuestIndicating(questIndicatingService.createQuestIndicating(questRunningLocation));

                questRunningLocationRepository.save(questRunningLocation);

                questRunningLocationDtos.add(QuestRunningLocationDto.questRunningLocationToQuestRunningLocationDto(questRunningLocation));
            }
        } else {
            // 이미 있어서 수정을 원하는 경우
            List<QuestRunningLocation> questRunningLocations = quest.getQuestRunningLocations();
            List<QuestRunningLocation> updatedQuestRunningLocations = upsertFromRequests(quest, questRunningLocations, questRunningLocationRequests);

            questRunningLocationDtos.addAll(updatedQuestRunningLocations.stream().map(QuestRunningLocationDto::questRunningLocationToQuestRunningLocationDto).toList());
        }

        return questRunningLocationDtos;
    }

    @Override
    public List<QuestRunningLocationDto> getQuestRunningLocations(Long questId) {
        Optional<Quest> questOptional = questRepository.findById(questId);
        if (questOptional.isEmpty())
            return null;

        Quest quest = questOptional.get();

        List<QuestRunningLocation> questRunningLocations = quest.getQuestRunningLocations();
        questRunningLocations.sort(Comparator.comparingInt(QuestRunningLocation::getSequence));

        return questRunningLocations.stream().map(QuestRunningLocationDto::questRunningLocationToQuestRunningLocationDto).collect(Collectors.toList());
    }

    @Override
    public void deleteQuestRunningLocation(Long questRunningLocationId) {
        questRunningLocationRepository.deleteById(questRunningLocationId);
    }

    private List<QuestRunningLocation> upsertFromRequests(Quest quest, List<QuestRunningLocation> questRunningLocations, List<QuestRunningLocationRequest> questRunningLocationRequests) {
        Map<Long, QuestRunningLocationRequest> reqById = questRunningLocationRequests.stream().filter(r -> r.getQuestRunningLocationId() != null)
                .collect(Collectors.toMap(QuestRunningLocationRequest::getQuestRunningLocationId, Function.identity()));

        Set<Long> entityIds = questRunningLocations.stream().map(QuestRunningLocation::getQuestRunningLocationId).collect(Collectors.toSet());

        for (QuestRunningLocation e : questRunningLocations) {
            // questRunningLocation에서 뽑은 Id가 Request에 있는 경우, 즉 중복되는 경우 업데이트
            QuestRunningLocationRequest r = reqById.get(e.getQuestRunningLocationId());
            if (r == null) continue;

            e.setSequence(r.getSequence());
            e.setLocationName(r.getLocationName());
            e.setDetailLocation(r.getDetailLocation());
            e.setLatitude(r.getLatitude());
            e.setLongitude(r.getLongitude());
            e.setAltitude(r.getAltitude());
        }

        for (QuestRunningLocationRequest r : questRunningLocationRequests) {
            Long id = r.getQuestRunningLocationId();
            if (id != null && entityIds.contains(id)) continue;

            QuestRunningLocation e = new QuestRunningLocation();
            e.setQuest(quest);
            e.setSequence(r.getSequence());
            e.setLocationName(r.getLocationName());
            e.setDetailLocation(r.getDetailLocation());
            e.setLatitude(r.getLatitude());
            e.setLongitude(r.getLongitude());
            e.setAltitude(r.getAltitude());

            questRunningLocationRepository.save(e);
            e.setQuestIndicating(questIndicatingService.createQuestIndicating(e));
            questRunningLocationRepository.save(e);

            questRunningLocations.add(e);
        }

        questRunningLocations.sort(Comparator.comparingInt(QuestRunningLocation::getSequence));

        return questRunningLocations;
    }
}
