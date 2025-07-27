package com.example.tabi.treasurehunt.treasurehuntpost.vo;

import com.example.tabi.postcounter.vo.PostCounterDto;
import com.example.tabi.reword.vo.RewordDto;
import com.example.tabi.treasurehunt.treasurehuntpostImage.vo.TreasureHuntPostImageDto;
import com.example.tabi.treasurehunt.treasurehuntstartlocation.vo.TreasureHuntLocationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreasureHuntPostDto {
    private Long treasureHuntPostId;

    private String uploadUserName;
    private String uploadUserProfileUrl;

    private String treasureHuntTitle;
    private String treasureHuntDescription;

    private boolean termination;
    private boolean locked;
    private boolean pub;

    private PostCounterDto postCounter;
    private RewordDto reword;
    private TreasureHuntLocationDto treasureHuntStartLocation;
    private TreasureHuntPostImageDto treasureHuntPostImage;

    private LocalDateTime createdAt;
}
