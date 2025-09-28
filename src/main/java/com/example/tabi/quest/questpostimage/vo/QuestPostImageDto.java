package com.example.tabi.quest.questpostimage.vo;

import com.example.tabi.quest.questpostimage.entity.QuestPostImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestPostImageDto {
    private Long questPostImageId;
    private String talkContent;
    private String characterImageUrl; // 엔티티 필드명과 동일
    private Long questPostId;

    public static QuestPostImageDto questPostImageToQuestPostImageDto(QuestPostImage questPostImage) {
        return new QuestPostImageDto(
                questPostImage.getQuestPostImageId(),
                questPostImage.getTalkContent(),
                questPostImage.getCharacterImageUrl(),
                questPostImage.getQuestPost().getQuestPostId()
        );
    }
}
