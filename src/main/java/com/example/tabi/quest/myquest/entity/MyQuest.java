package com.example.tabi.quest.myquest.entity;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.postcounter.entity.PostCounter;
import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.reward.entity.Reward;
import com.example.tabi.util.PostStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;

import java.time.LocalDateTime;

@Entity
@Data
public class MyQuest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long myQuestId;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "quest_post_id")
    private QuestPost questPost;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
