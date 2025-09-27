package com.example.tabi.quest.quest.service;

import com.example.tabi.quest.quest.entity.Quest;
import com.example.tabi.quest.quest.repository.QuestRepository;
import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.quest.questpost.repository.QuestPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestServiceJpaImpl implements QuestService {
    private final QuestRepository questRepository;
    private final QuestPostRepository questPostRepository;

    @Override
    public Quest createQuest(QuestPost questPost) {
        Quest quest = new Quest();

        quest.setQuestPost(questPost);
        questRepository.save(quest);

        return quest;
    }
}
