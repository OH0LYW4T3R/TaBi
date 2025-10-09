package com.example.tabi.quest.questpostimage.service;

import com.example.tabi.quest.actions.action.entity.Action;
import com.example.tabi.quest.actions.talkingaction.entity.TalkingAction;
import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.quest.questpostimage.entity.QuestPostImage;
import com.example.tabi.quest.questpostimage.repository.QuestPostImageRepository;
import com.example.tabi.quest.questrunninglocation.entity.QuestRunningLocation;
import com.example.tabi.quest.queststep.entity.QuestStep;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestPostImageServiceJpaImpl implements QuestPostImageService {
    private final QuestPostImageRepository questPostImageRepository;
    private final static Integer MAX_PAGE = 3;

    @Override
    public List<QuestPostImage> createQuestPostImages(QuestPost questPost) {
        List<QuestPostImage> questPostImages = new ArrayList<>();
        List<Thumbnail> result = extractContentAndImage(questPost);

        for (Thumbnail thumbnail : result) {
            QuestPostImage questPostImage = new QuestPostImage();

            questPostImage.setQuestPost(questPost);

            questPostImage.setTalkContent(thumbnail.getTalkContent());
            questPostImage.setCharacterImageUrl(thumbnail.getCharacterImageUrl());

            questPostImageRepository.save(questPostImage);
            questPostImages.add(questPostImage);
        }

        return questPostImages;
    }

    private List<Thumbnail> extractContentAndImage(QuestPost questPost) {
        List<Thumbnail> extractedResult = new ArrayList<>();

        List<QuestRunningLocation> forExtract = new ArrayList<>();
        questPost.getQuest().getQuestRunningLocations().sort(Comparator.comparingInt(QuestRunningLocation::getSequence));
        forExtract.add(questPost.getQuest().getQuestRunningLocations().get(0));

        if (questPost.getQuest().getQuestRunningLocations().get(1) != null) {
            forExtract.add(questPost.getQuest().getQuestRunningLocations().get(1));
        }

        for (QuestRunningLocation questRunningLocation : forExtract) {
            List<QuestStep> questSteps = questRunningLocation.getQuestIndicating().getQuestSteps();
            questSteps.sort(Comparator.comparingInt(QuestStep::getSequence));

            int counter = 0;
            for (QuestStep questStep : questSteps) {
                if (counter >= MAX_PAGE) break; // 대화 내용 3개만 저장

                Action action = questStep.getAction();

                if (action instanceof TalkingAction) {
                    Thumbnail thumbnail = new Thumbnail(((TalkingAction) action).getStory(), action.getCharacterImageUrl());
                    extractedResult.add(thumbnail);
                }

                counter++;
            }
        }

        return extractedResult;
    }

    @AllArgsConstructor
    @Data
    private static class Thumbnail {
        public String talkContent;
        public String characterImageUrl;
    }
}
