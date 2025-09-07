package com.example.tabi.quest.questrunninglocation.entity;

import com.example.tabi.quest.quest.entity.Quest;
import com.example.tabi.quest.questindicating.entity.QuestIndicating;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class QuestRunningLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questRunningLocationId;

    private Integer sequence; // 순서 번호
    private String locationName; // 보여줄 주소 (ex 한밭대학교)
    private String detailLocation; // 실 주소 (ex 대전광역시 유성구 ...)

    private double latitude;
    private double longitude;
    private double altitude;

    @OneToOne(mappedBy = "questRunningLocation", cascade = CascadeType.ALL, orphanRemoval = true)
    private QuestIndicating questIndicating;

    // 관계의 주인
    @ManyToOne
    @JoinColumn(name = "quest_id")
    private Quest quest;
}
