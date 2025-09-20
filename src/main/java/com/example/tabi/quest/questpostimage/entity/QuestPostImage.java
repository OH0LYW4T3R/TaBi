package com.example.tabi.quest.questpostimage.entity;

import com.example.tabi.quest.questpost.entity.QuestPost;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class QuestPostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questPostImageId;

    private String talkContent;
    private String characterImageUrl;

    @ManyToOne
    @JoinColumn(name = "quest_post_id")
    private QuestPost questPost;
}
