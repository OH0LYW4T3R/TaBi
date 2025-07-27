package com.example.tabi.treasurehunt.treasurehuntpostImage.service;

import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import com.example.tabi.treasurehunt.treasurehuntpostImage.entity.TreasureHuntPostImage;
import com.example.tabi.treasurehunt.treasurehuntpostImage.vo.TreasureHuntPostImageDto;
import org.springframework.security.core.Authentication;

public interface TreasureHuntPostImageService {
    TreasureHuntPostImage createTreasureHuntPostImage(TreasureHuntPost treasureHuntPost, String imageUrl);
}
