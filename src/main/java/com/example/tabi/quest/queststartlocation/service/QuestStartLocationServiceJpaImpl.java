package com.example.tabi.quest.queststartlocation.service;

import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.quest.questrunninglocation.entity.QuestRunningLocation;
import com.example.tabi.quest.queststartlocation.entity.QuestStartLocation;
import com.example.tabi.quest.queststartlocation.repository.QuestStartLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestStartLocationServiceJpaImpl implements QuestStartLocationService {
    private final QuestStartLocationRepository questStartLocationRepository;

    @Override
    public QuestStartLocation createQuestStartLocation(QuestPost questPost) {
        QuestStartLocation questStartLocation = new QuestStartLocation();
        QuestRunningLocation questRunningLocation = questPost.getQuest().getQuestRunningLocations().get(0); // 시작 위치 (실행위치의 처음것)

        questStartLocation.setQuestPost(questPost);

        questStartLocation.setActualLocation(questRunningLocation.getDetailLocation());
        questStartLocation.setIndicateLocation(questRunningLocation.getLocationName());

        questStartLocation.setLatitude(questRunningLocation.getLatitude());
        questStartLocation.setLongitude(questRunningLocation.getLongitude());
        questStartLocation.setAltitude(questRunningLocation.getAltitude());

        questStartLocationRepository.save(questStartLocation);

        return questStartLocation;
    }
}
