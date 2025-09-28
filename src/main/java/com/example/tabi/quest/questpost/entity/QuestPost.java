package com.example.tabi.quest.questpost.entity;

import com.example.tabi.postcounter.entity.PostCounter;
import com.example.tabi.quest.myquest.entity.MyQuest;
import com.example.tabi.quest.quest.entity.Quest;
import com.example.tabi.quest.questpostimage.entity.QuestPostImage;
import com.example.tabi.quest.queststartlocation.entity.QuestStartLocation;
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

    // 예상 소요시간
    private Integer estimatedDay;
    private Integer estimatedHour;
    private Integer estimatedMinute;
    // 예상 소요시간

    private boolean locked;
    private boolean pub;

    private boolean init;
    private boolean fin;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_counter_id")
    private PostCounter postCounter;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reword_id")
    private Reward reward;


    @OneToMany(mappedBy = "questPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<QuestPostImage> questPostImages = new ArrayList<>();

    @OneToOne(mappedBy = "questPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private QuestStartLocation questStartLocation;

    // 참여 기록 (MyQuest)
    @OneToMany(mappedBy = "questPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MyQuest> myQuests = new ArrayList<>();

    // 퀘스트의 실제 정보 및 세부정보
    @OneToOne(mappedBy = "questPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private Quest quest;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
