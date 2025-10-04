package com.example.tabi.quest.myquestplay.vo;

import com.example.tabi.quest.myquestplay.entity.MyQuestPlay;
import com.example.tabi.util.PlayStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyQuestPlayDto {
    private Long myQuestPlayId;
    private Long appUserId;
    private Long questPostId;
    private PlayStatus playStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String errorMessage;

    public static MyQuestPlayDto myQuestPlayToMyQuestPlayDto(MyQuestPlay myQuestPlay) {
        return new MyQuestPlayDto(
                myQuestPlay.getMyQuestPlayId(),
                myQuestPlay.getAppUser().getAppUserId(),
                myQuestPlay.getQuestPost().getQuestPostId(),
                myQuestPlay.getPlayStatus(),
                myQuestPlay.getCreatedAt(),
                myQuestPlay.getUpdatedAt(),
                null
        );

    }
}
