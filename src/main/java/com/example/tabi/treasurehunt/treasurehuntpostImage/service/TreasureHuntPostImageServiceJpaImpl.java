package com.example.tabi.treasurehunt.treasurehuntpostImage.service;

import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import com.example.tabi.treasurehunt.treasurehuntpostImage.entity.TreasureHuntPostImage;
import com.example.tabi.treasurehunt.treasurehuntpostImage.repository.TreasureHuntPostImageRepository;
import com.example.tabi.treasurehunt.treasurehuntpostImage.vo.TreasureHuntPostImageDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TreasureHuntPostImageServiceJpaImpl implements TreasureHuntPostImageService {
    private final TreasureHuntPostImageRepository treasureHuntPostImageRepository;

    @Override
    @Transactional
    public TreasureHuntPostImage createTreasureHuntPostImage(TreasureHuntPost treasureHuntPost, String imageUrl) {
        TreasureHuntPostImage treasureHuntPostImage = new TreasureHuntPostImage();
        treasureHuntPostImage.setImageUrl(imageUrl);
        treasureHuntPostImage.setTreasureHuntPost(treasureHuntPost);
        treasureHuntPostImageRepository.save(treasureHuntPostImage);

        return treasureHuntPostImage;
    }

    public static TreasureHuntPostImageDto treasureHuntPostImageToTreasureHuntPostImageDto(TreasureHuntPostImage entity) {
        if (entity == null) return null;

        return new TreasureHuntPostImageDto(
                entity.getTreasureHuntPostImageId(),
                entity.getImageUrl()
        );
    }
}
