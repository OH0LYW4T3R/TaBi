package com.example.tabi.quest.myquestplay.entity;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.quest.myquestplay.trace.questcurrentpoint.entity.QuestCurrentPoint;
import com.example.tabi.quest.myquestplay.trace.questhintsave.entity.QuestHintSave;
import com.example.tabi.quest.myquestplay.trace.questsavepoint.entity.QuestSavePoint;
import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.util.PlayStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class MyQuestPlay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long myQuestPlayId;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "quest_post_id")
    private QuestPost questPost;

    private PlayStatus playStatus;

    // Trace
    @OneToOne(mappedBy = "myQuestPlay", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private QuestCurrentPoint questCurrentPoint;

    @OneToOne(mappedBy = "myQuestPlay", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private QuestSavePoint questSavePoint;

    @OneToMany(mappedBy = "myQuestPlay", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<QuestHintSave> questHintSaves = new ArrayList<>();
    // Trace

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
