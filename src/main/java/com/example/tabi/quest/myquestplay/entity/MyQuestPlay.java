package com.example.tabi.quest.myquestplay.entity;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.util.PlayStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

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

    //힌트 해금여부 저장하는 클래스..

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
