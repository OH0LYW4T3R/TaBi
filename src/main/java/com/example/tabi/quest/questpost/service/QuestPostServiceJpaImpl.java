package com.example.tabi.quest.questpost.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.repository.AppUserRepository;
import com.example.tabi.appuser.service.AppUserServiceJpaImpl;
import com.example.tabi.member.repository.MemberRepository;
import com.example.tabi.postcounter.entity.PostCounter;
import com.example.tabi.postcounter.service.PostCounterService;
import com.example.tabi.quest.myquest.entity.MyQuest;
import com.example.tabi.quest.myquest.repository.MyQuestRepository;
import com.example.tabi.quest.myquest.service.MyQuestService;
import com.example.tabi.quest.quest.service.QuestService;
import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.quest.questpost.repository.QuestPostRepository;
import com.example.tabi.quest.questpost.vo.FinalSettingQuestPostRequest;
import com.example.tabi.quest.questpost.vo.FullQuestPostDto;
import com.example.tabi.quest.questpost.vo.QuestPostDto;
import com.example.tabi.quest.questpostimage.entity.QuestPostImage;
import com.example.tabi.quest.questpostimage.repository.QuestPostImageRepository;
import com.example.tabi.quest.questpostimage.service.QuestPostImageService;
import com.example.tabi.quest.queststartlocation.service.QuestStartLocationService;
import com.example.tabi.reward.entity.Reward;
import com.example.tabi.reward.service.RewardService;
import com.example.tabi.util.PostStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestPostServiceJpaImpl implements QuestPostService {
    private final QuestPostRepository questPostRepository;
    private final MyQuestRepository myQuestRepository;
    private final AppUserRepository appUserRepository;
    private final MemberRepository memberRepository;

    private final RewardService rewardService;
    private final PostCounterService postCounterService;
    private final QuestService questService;
    private final MyQuestService myQuestService;
    private final QuestPostImageService questPostImageService;
    private final QuestStartLocationService questStartLocationService;

    @Override
    @Transactional
    public QuestPostDto initialSettingQuestPost(Authentication authentication) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();

        QuestPost questPost = new QuestPost();
        questPost.setUploadUserName(appUser.getMyProfile().getNickName());
        questPost.setUploadUserProfileUrl(appUser.getMyProfile().getProfileImageUrl());
        questPost.setPub(true);
        questPost.setLocked(true); // init 셋팅에서는 보여주지 않기 위해
        questPost.setInit(true);
        questPost.setFin(false);

        questPostRepository.save(questPost);

        questPost.setQuest(questService.createQuest(questPost));

        // 관계 연결
        Reward reward = rewardService.createReward(true);
        questPost.setReward(reward);
        PostCounter postCounter = postCounterService.createPostCounter();
        questPost.setPostCounter(postCounter);
        // 관계 연결

        questPostRepository.save(questPost);

        // 생성한 포스트와 사용자 연결
        myQuestService.createMyQuest(appUser, questPost);

        return QuestPostDto.questPostToQuestPostDto(questPost);
    }

    @Override
    @Transactional
    public FullQuestPostDto finalSettingQuestPost(FinalSettingQuestPostRequest finalSettingQuestPostRequest) {
        Optional<QuestPost> questPostOptional = questPostRepository.findById(finalSettingQuestPostRequest.getQuestPostId());
        if (questPostOptional.isEmpty()) return null;

        QuestPost questPost = questPostOptional.get();

        if (questPost.isFin()) return null; // 이미 작업이 모두 완료된 상태.

        questPost.setQuestTitle(finalSettingQuestPostRequest.getQuestTitle());
        questPost.setQuestDescription(finalSettingQuestPostRequest.getQuestDescription());

        if (finalSettingQuestPostRequest.getEstimatedDay() != null && finalSettingQuestPostRequest.getEstimatedHour() != null && finalSettingQuestPostRequest.getEstimatedMinute() != null) {
            questPost.setEstimatedDay(finalSettingQuestPostRequest.getEstimatedDay());
            questPost.setEstimatedHour(finalSettingQuestPostRequest.getEstimatedHour());
            questPost.setEstimatedMinute(finalSettingQuestPostRequest.getEstimatedMinute());
        }
        else {
            questPost.setEstimatedDay(null);
            questPost.setEstimatedHour(null);
            questPost.setEstimatedMinute(null);
        }

        // 관계 연결
        List<QuestPostImage> managedImages = questPost.getQuestPostImages(); // 영속 컬렉션 (이미 존재)
        managedImages.clear(); // 기존 요소 제거 (orphanRemoval 동작)

        for (QuestPostImage img : questPostImageService.createQuestPostImages(questPost)) {
            img.setQuestPost(questPost);
            managedImages.add(img);
        }

        questPost.setQuestStartLocation(questStartLocationService.createQuestStartLocation(questPost));
        // 관계 연결

        questPost.setPub(finalSettingQuestPostRequest.isPub());
        questPost.setLocked(false);
        questPost.setFin(true);

        questPostRepository.save(questPost);

        return FullQuestPostDto.questPostToFullQuestPostDto(questPost);
    }

    // action까지 전체 다보여주는 함수랑 축소해서 보여주는 함수 둘다 만들어서 체킹하기.

    @Override
    public List<QuestPostDto> readTenQuestPosts(Authentication authentication, int pages) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();

        PageRequest page = PageRequest.of(pages, 10, Sort.by("createdAt").descending().and(Sort.by("questPostId").descending()));
        Page<QuestPost> questPosts = questPostRepository.findVisiblePostsNotCreatedBy(appUser.getMyProfile().getNickName(), page);

        return questPosts.getContent().stream().map(QuestPostDto::questPostToQuestPostDto).toList();
    }

    @Override
    public String playQuestPost(Authentication authentication, Long questPostId) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return "User Not Found";

        AppUser appUser = optionalAppUser.get();

        Optional<QuestPost> optionalQuestPost = questPostRepository.findById(questPostId);

        if (optionalQuestPost.isEmpty())
            return "Quest Post Not Found";
        QuestPost questPost = optionalQuestPost.get();

        Optional<MyQuest> myQuestOptional = myQuestRepository.findByAppUserAndQuestPost(appUser, questPost);

        if (myQuestOptional.isPresent()) {
            MyQuest myQuest = myQuestOptional.get();

            if (myQuest.getStatus() == PostStatus.CREATED)
                return "The creator cannot run.";

            if (myQuest.getStatus() == PostStatus.RUNNING)
                return "This is a post that is already running.";

            if (myQuest.getStatus() == PostStatus.TERMINATED)
                return "This post has been terminated.";

            if (myQuest.getStatus() == PostStatus.SAVED) {
                myQuestRepository.delete(myQuest); // 저장된건 삭제하고 플레이로 넘김
            }
        }

        myQuestService.playMyQuest(appUser, questPost);

        return "success";
    }
}
