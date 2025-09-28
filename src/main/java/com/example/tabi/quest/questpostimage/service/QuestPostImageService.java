package com.example.tabi.quest.questpostimage.service;

import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.quest.questpostimage.entity.QuestPostImage;

import java.util.List;

public interface QuestPostImageService {
    List<QuestPostImage> createQuestPostImages(QuestPost questPost);
}
