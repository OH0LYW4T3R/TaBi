package com.example.tabi.treasurehunt.treasurehuntpost.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.repository.AppUserRepository;
import com.example.tabi.appuser.service.AppUserServiceJpaImpl;
import com.example.tabi.member.repository.MemberRepository;
import com.example.tabi.postcounter.vo.PostCounterDto;
import com.example.tabi.reword.vo.RewordDto;
import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import com.example.tabi.treasurehunt.treasurehuntpost.repository.TreasureHuntPostRepository;
import com.example.tabi.treasurehunt.treasurehuntpost.service.TreasureHuntPostService;
import com.example.tabi.treasurehunt.treasurehuntpost.vo.TreasureHuntPostDto;
import com.example.tabi.treasurehunt.treasurehuntpost.vo.TreasureHuntPostRequest;
import com.example.tabi.treasurehunt.treasurehuntpostImage.entity.TreasureHuntPostImage;
import com.example.tabi.treasurehunt.treasurehuntpostImage.service.TreasureHuntPostImageService;
import com.example.tabi.treasurehunt.treasurehuntpostImage.service.TreasureHuntPostImageServiceJpaImpl;
import com.example.tabi.treasurehunt.treasurehuntpostImage.vo.TreasureHuntPostImageDto;
import com.example.tabi.treasurehunt.treasurehuntstartlocation.entity.TreasureHuntLocation;
import com.example.tabi.treasurehunt.treasurehuntstartlocation.service.TreasureHuntLocationService;
import com.example.tabi.treasurehunt.treasurehuntstartlocation.service.TreasureHuntLocationServiceJpaImpl;
import com.example.tabi.treasurehunt.treasurehuntstartlocation.vo.TreasureHuntLocationDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TreasureHuntPostServiceJpaImpl implements TreasureHuntPostService {
    private final TreasureHuntPostRepository treasureHuntPostRepository;
    private final TreasureHuntPostImageService treasureHuntPostImageService;
    private final TreasureHuntLocationService treasureHuntLocationService;
    private final AppUserRepository appUserRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public TreasureHuntPostDto createTreasureHuntPost(Authentication authentication, TreasureHuntPostRequest treasureHuntPostRequest) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();

        TreasureHuntPost treasureHuntPost = new TreasureHuntPost();
        treasureHuntPost.setUploadUserName(appUser.getMyProfile().getNickName());
        treasureHuntPost.setUploadUserProfileUrl(appUser.getMyProfile().getProfileImageUrl());
        treasureHuntPost.setTreasureHuntTitle(treasureHuntPostRequest.getTreasureHuntTitle());
        treasureHuntPost.setTreasureHuntDescription(treasureHuntPostRequest.getTreasureHuntDescription());

        treasureHuntPost.setTermination(false);
        treasureHuntPost.setLocked(false);
        treasureHuntPost.setPub(treasureHuntPostRequest.getIsPublic());

        treasureHuntPostRepository.save(treasureHuntPost);

        TreasureHuntPostImage treasureHuntPostImage = treasureHuntPostImageService.createTreasureHuntPostImage(treasureHuntPost, treasureHuntPostRequest.getImageUrl());
        treasureHuntPost.setTreasureHuntPostImages(treasureHuntPostImage);
        TreasureHuntLocation treasureHuntLocation = treasureHuntLocationService.createTreasureHuntLocation(treasureHuntPost, treasureHuntPostRequest.getLatitude(), treasureHuntPostRequest.getLongitude(), treasureHuntPostRequest.getAltitude());
        treasureHuntPost.setTreasureHuntStartLocation(treasureHuntLocation);

        // reword, postcounter, myTreasureHunt 추가 해야함.

        treasureHuntPostRepository.save(treasureHuntPost);

        return treasureHuntPostToTreasureHuntPostDto(
                treasureHuntPost,
                null,
                null,
                TreasureHuntLocationServiceJpaImpl.treasureHuntLocationToTreasureHuntLocationDto(treasureHuntLocation),
                TreasureHuntPostImageServiceJpaImpl.treasureHuntPostImageToTreasureHuntPostImageDto(treasureHuntPostImage)
        );
    }

    public static TreasureHuntPostDto treasureHuntPostToTreasureHuntPostDto(TreasureHuntPost post, PostCounterDto postCounterDto, RewordDto rewordDto, TreasureHuntLocationDto locationDto, TreasureHuntPostImageDto imageDto) {
        if (post == null) return null;

        return new TreasureHuntPostDto(
                post.getTreasureHuntPostId(),
                post.getUploadUserName(),
                post.getUploadUserProfileUrl(),
                post.getTreasureHuntTitle(),
                post.getTreasureHuntDescription(),
                post.isTermination(),
                post.isLocked(),
                post.isPub(),
                postCounterDto,
                rewordDto,
                locationDto,
                imageDto,
                post.getCreatedAt()
        );
    }
}
