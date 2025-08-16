package com.example.tabi.treasurehunt.treasurehuntpost.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.repository.AppUserRepository;
import com.example.tabi.appuser.service.AppUserServiceJpaImpl;
import com.example.tabi.member.repository.MemberRepository;
import com.example.tabi.postcounter.entity.PostCounter;
import com.example.tabi.postcounter.service.PostCounterService;
import com.example.tabi.postcounter.service.PostCounterServiceJpaImpl;
import com.example.tabi.postcounter.vo.PostCounterDto;
import com.example.tabi.reward.entity.Reward;
import com.example.tabi.reward.service.RewardService;
import com.example.tabi.reward.service.RewardServiceJpaImpl;
import com.example.tabi.reward.vo.RewardDto;
import com.example.tabi.treasurehunt.mytreasurehunt.PostStatus;
import com.example.tabi.treasurehunt.mytreasurehunt.entity.MyTreasureHunt;
import com.example.tabi.treasurehunt.mytreasurehunt.repository.MyTreasureHuntRepository;
import com.example.tabi.treasurehunt.mytreasurehunt.service.MyTreasureHuntService;
import com.example.tabi.treasurehunt.mytreasurehuntplay.vo.PositionRequest;
import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import com.example.tabi.treasurehunt.treasurehuntpost.repository.TreasureHuntPostRepository;
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
import com.example.tabi.util.FileUploadUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TreasureHuntPostServiceJpaImpl implements TreasureHuntPostService {
    private final TreasureHuntPostRepository treasureHuntPostRepository;
    private final TreasureHuntPostImageService treasureHuntPostImageService;
    private final TreasureHuntLocationService treasureHuntLocationService;
    private final MyTreasureHuntRepository myTreasureHuntRepository;
    private final MyTreasureHuntService myTreasureHuntService;
    private final RewardService rewardService;
    private final PostCounterService postCounterService;
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

        String saveImageUrl = null;
        try {
            saveImageUrl = FileUploadUtil.saveImage(treasureHuntPostRequest.getImage(), appUser.getAppUserId().toString(), "TreasureHuntPosting");
        } catch (IOException e) {
            System.out.println("잘못된 이미지 지정 혹은 허용되지 않은 이미지 확장자");
            return null;
        }

        TreasureHuntPostImage treasureHuntPostImage = treasureHuntPostImageService.createTreasureHuntPostImage(treasureHuntPost, saveImageUrl);
        treasureHuntPost.setTreasureHuntPostImages(treasureHuntPostImage);
        TreasureHuntLocation treasureHuntLocation = treasureHuntLocationService.createTreasureHuntLocation(treasureHuntPost, treasureHuntPostRequest.getLatitude(), treasureHuntPostRequest.getLongitude(), treasureHuntPostRequest.getAltitude());
        treasureHuntPost.setTreasureHuntStartLocation(treasureHuntLocation);

        Reward reward = rewardService.createReward(false);
        treasureHuntPost.setReward(reward);
        PostCounter postCounter = postCounterService.createPostCounter();
        treasureHuntPost.setPostCounter(postCounter);

        treasureHuntPostRepository.save(treasureHuntPost);

        // 생성한 포스트와 사용자 연결
        myTreasureHuntService.createMyTreasureHunt(appUser, treasureHuntPost);

        return treasureHuntPostToTreasureHuntPostDto(
                treasureHuntPost,
                postCounter,
                reward,
                treasureHuntLocation,
                treasureHuntPostImage
        );
    }

    @Override
    public List<TreasureHuntPostDto> readTenTreasureHuntPosts(Authentication authentication, int pages) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();

        PageRequest page = PageRequest.of(pages, 10, Sort.by("createdAt").descending().and(Sort.by("treasureHuntPostId").descending()));
        Page<TreasureHuntPost> posts = treasureHuntPostRepository.findVisiblePostsNotCreatedBy(appUser.getMyProfile().getNickName(), page);

        return posts.getContent().stream()
            .map(post -> TreasureHuntPostServiceJpaImpl.treasureHuntPostToTreasureHuntPostDto(post, post.getPostCounter(), post.getReward(), post.getTreasureHuntStartLocation(), post.getTreasureHuntPostImages())).toList();
    }

    @Override
    public TreasureHuntPostDto getTreasureHuntPostById(Long id) {
       Optional<TreasureHuntPost> optionalTreasureHuntPost = treasureHuntPostRepository.findById(id);

       if (optionalTreasureHuntPost.isEmpty())
            return null;

       TreasureHuntPost treasureHuntPost = optionalTreasureHuntPost.get();

       return treasureHuntPostToTreasureHuntPostDto(treasureHuntPost, treasureHuntPost.getPostCounter(), treasureHuntPost.getReward(), treasureHuntPost.getTreasureHuntStartLocation(), treasureHuntPost.getTreasureHuntPostImages());
    }

    @Override
    public String playTreasureHuntPost(Authentication authentication, PositionRequest positionRequest) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return "User Not Found";

        AppUser appUser = optionalAppUser.get();

        Optional<TreasureHuntPost> optionalTreasureHuntPost = treasureHuntPostRepository.findById(positionRequest.getTreasureHuntPostId());

       if (optionalTreasureHuntPost.isEmpty())
            return "Treasure Hunt Post Not Found";

       TreasureHuntPost treasureHuntPost = optionalTreasureHuntPost.get();

       Optional<MyTreasureHunt> myTreasureHunts = myTreasureHuntRepository.findByAppUserAndTreasureHuntPost(appUser, treasureHuntPost);

       if (myTreasureHunts.isPresent()) {
           MyTreasureHunt myTreasureHunt = myTreasureHunts.get();

            if (myTreasureHunt.getStatus() == PostStatus.CREATED)
                return "The creator cannot run.";

            if (myTreasureHunt.getStatus() == PostStatus.RUNNING)
                return "This is a post that is already running.";

            if (myTreasureHunt.getStatus() == PostStatus.TERMINATED)
                return "This post has been terminated.";

            if (myTreasureHunt.getStatus() == PostStatus.SAVED) {
                myTreasureHuntRepository.delete(myTreasureHunt); // 저장된건 삭제하고 플레이로 넘김
            }
       }

       myTreasureHuntService.playMyTreasureHunt(appUser, treasureHuntPost);

       return "success";
    }

    public static TreasureHuntPostDto treasureHuntPostToTreasureHuntPostDto(TreasureHuntPost post, PostCounter postCounter, Reward reward, TreasureHuntLocation treasureHuntLocation, TreasureHuntPostImage treasureHuntPostImage) {
        return new TreasureHuntPostDto(
                post.getTreasureHuntPostId(),
                post.getUploadUserName(),
                post.getUploadUserProfileUrl(),
                post.getTreasureHuntTitle(),
                post.getTreasureHuntDescription(),
                post.isTermination(),
                post.isLocked(),
                post.isPub(),
                PostCounterServiceJpaImpl.postCounterToPostCounterDto(postCounter),
                RewardServiceJpaImpl.rewardToRewardDto(reward),
                TreasureHuntLocationServiceJpaImpl.treasureHuntLocationToTreasureHuntLocationDto(treasureHuntLocation),
                TreasureHuntPostImageServiceJpaImpl.treasureHuntPostImageToTreasureHuntPostImageDto(treasureHuntPostImage),
                post.getCreatedAt()
        );
    }
}
