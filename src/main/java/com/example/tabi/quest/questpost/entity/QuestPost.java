package com.example.tabi.quest.questpost.entity;

import com.example.tabi.postcounter.entity.PostCounter;
import com.example.tabi.quest.myquest.entity.MyQuest;
import com.example.tabi.reward.entity.Reward;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class QuestPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questPostId;

    private String uploadUserName;
    private String uploadUserProfileUrl;

    private String questTitle;
    private String questDescription;

    private LocalDateTime estimatedTime; // 예상 소요시간

    private boolean locked;
    private boolean pub;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_counter_id")
    private PostCounter postCounter;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reword_id")
    private Reward reward;

    @OneToMany(mappedBy = "questPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MyQuest> myQuests = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;
}
