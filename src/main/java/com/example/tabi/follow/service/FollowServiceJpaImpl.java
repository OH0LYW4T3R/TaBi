package com.example.tabi.follow.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.repository.AppUserRepository;
import com.example.tabi.appuser.service.AppUserServiceJpaImpl;
import com.example.tabi.follow.entity.Follow;
import com.example.tabi.follow.repository.FollowRepository;
import com.example.tabi.follow.vo.FollowDto;
import com.example.tabi.follow.vo.FollowStatus;
import com.example.tabi.follow.vo.RetrieveProfileDto;
import com.example.tabi.follow.vo.RetrieveProfileRequest;
import com.example.tabi.member.repository.MemberRepository;
import com.example.tabi.myprofile.entity.MyProfile;
import com.example.tabi.myprofile.repository.MyProfileRepository;
import com.example.tabi.myprofile.vo.FollowPolicy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowServiceJpaImpl implements FollowService {
    public static final long SCHEDULE_TIME = 60 * 60 * 1000L;
    private static final long DECLINED_TTL_HOURS = 24L;

    private final FollowRepository followRepository;
    private final AppUserRepository appUserRepository;
    private final MemberRepository memberRepository;
    private final MyProfileRepository myProfileRepository;

    @Override
    @Transactional
    public FollowDto followRequest(Authentication authentication, Long followeeId) {
        FollowDto followForErrorDto = new FollowDto();

        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty()) {
            followForErrorDto.setErrorMessage("AppUser Not Found");
            return followForErrorDto;
        }

        AppUser appUser = optionalAppUser.get();

        Optional<AppUser> optionalFollowee = appUserRepository.findById(followeeId);

        if (optionalFollowee.isEmpty()) {
            followForErrorDto.setErrorMessage("Followee Not Found");
            return followForErrorDto;
        }

        AppUser followee = optionalFollowee.get();

        if (appUser.getAppUserId().equals(followee.getAppUserId())) {
            followForErrorDto.setErrorMessage("Cannot Follow MySelf");
            return followForErrorDto;
        }

        String logicCheck = checkFollowStatus(appUser, followee, FollowStatus.REQUESTED);

        if (logicCheck != null) {
            followForErrorDto.setErrorMessage(logicCheck);
            return followForErrorDto;
        }

        Follow follow = new Follow();
        follow.setFollower(appUser);
        follow.setFollowee(followee);

        if (followee.getMyProfile().getFollowPolicy() == FollowPolicy.AUTO_ACCEPT) {
            // 자동 수락인 경우
            follow.setFollowStatus(FollowStatus.FOLLOWED);

            appUser.getMyProfile().setFollowingCount(appUser.getMyProfile().getFollowingCount() + 1);
            appUserRepository.save(appUser);
            followee.getMyProfile().setFollowerCount(followee.getMyProfile().getFollowerCount() + 1);
            appUserRepository.save(followee);

            reverseFollow(appUser, followee);
        } else {
            // 승인모드인 경우
            Optional<Follow> optionalRequestFollow = followRepository.findByFollowerAndFolloweeAndFollowStatus(followee, appUser, FollowStatus.REQUESTED);

            if (optionalRequestFollow.isEmpty()) {
                follow.setFollowStatus(FollowStatus.REQUESTED);
            } else {
                follow.setFollowStatus(FollowStatus.FOLLOWED);
                reverseFollow(appUser, followee);
            }

            appUser.getMyProfile().setFollowingCount(appUser.getMyProfile().getFollowingCount() + 1);
            appUserRepository.save(appUser);
            followee.getMyProfile().setFollowerCount(followee.getMyProfile().getFollowerCount() + 1);
            appUserRepository.save(followee);
        }

        followRepository.save(follow);

        return FollowDto.followToFollowDto(follow);
    }

    @Override
    @Transactional
    public String followCancel(Authentication authentication, Long followeeId) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return "AppUser Not Found";

        AppUser appUser = optionalAppUser.get();

        Optional<AppUser> optionalFollowee = appUserRepository.findById(followeeId);

        if (optionalFollowee.isEmpty())
            return "Followee Not Found";

        AppUser followee = optionalFollowee.get();

        if (appUser.getAppUserId().equals(followee.getAppUserId()))
            return "Cannot Unfollow MySelf";

        // 상대방 기준
        Optional<Follow> optionalFollow = followRepository.findByFollowerAndFollowee(followee, appUser);

        if (optionalFollow.isEmpty())
            return "Relation Not Found";

        Follow follow = optionalFollow.get();

        if (follow.getFollowStatus() == FollowStatus.FOLLOWED) {
            followRepository.delete(follow);
            // 카운트 감소: B following--, A follower--
            followee.getMyProfile().setFollowingCount(followee.getMyProfile().getFollowingCount() - 1);
            appUser.getMyProfile().setFollowerCount(appUser.getMyProfile().getFollowerCount() - 1);

            Optional<Follow> optionalPairFollow = followRepository.findByFollowerAndFolloweeAndFollowStatus(appUser, followee, FollowStatus.FOLLOWED);
            if (optionalPairFollow.isPresent()) {
                Follow pairFollow = optionalPairFollow.get();
                followRepository.delete(pairFollow);

                // 카운트 감소: A following--, B follower--
                appUser.getMyProfile().setFollowingCount(appUser.getMyProfile().getFollowingCount() - 1);
                followee.getMyProfile().setFollowerCount(followee.getMyProfile().getFollowerCount() - 1);
            }

            appUserRepository.save(appUser);
            appUserRepository.save(followee);

            return "Success Unfollow";
        }

        return "Unable to request (BLOCK or DECLINED)";
    }

    @Override
    public List<RetrieveProfileDto> retrieveMyFollowers(Authentication authentication) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);
        if (optionalAppUser.isEmpty()) return List.of();

        AppUser me = optionalAppUser.get();

        List<Follow> followerEdges = followRepository.findByFolloweeAndFollowStatus(me, FollowStatus.FOLLOWED);

        return followerEdges.stream().map(edge -> edge.getFollower().getMyProfile()).map(RetrieveProfileDto::createRetrieveProfileDto).toList();
    }

    @Override
    public List<RetrieveProfileDto> retrieveMyFollowings(Authentication authentication) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);
        if (optionalAppUser.isEmpty()) return List.of();

        AppUser me = optionalAppUser.get();

        List<Follow> followingEdges = followRepository.findByFollowerAndFollowStatus(me, FollowStatus.FOLLOWED);

        return followingEdges.stream().map(edge -> edge.getFollowee().getMyProfile()).map(RetrieveProfileDto::createRetrieveProfileDto).toList();
    }

    @Override
    @Transactional
    public FollowDto blockRequest(Authentication authentication, Long followeeId) {
        FollowDto followForErrorDto = new FollowDto();
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty()) {
            followForErrorDto.setErrorMessage("AppUser Not Found");
            return followForErrorDto;
        }

        AppUser appUser = optionalAppUser.get();

        Optional<AppUser> optionalFollowee = appUserRepository.findById(followeeId);

        if (optionalFollowee.isEmpty()) {
            followForErrorDto.setErrorMessage("Followee Not Found");
            return followForErrorDto;
        }

        AppUser followee = optionalFollowee.get();

        if (appUser.getAppUserId().equals(followee.getAppUserId())) {
            followForErrorDto.setErrorMessage("Cannot Follow MySelf");
            return followForErrorDto;
        }

        String check = checkFollowStatus(appUser, followee, FollowStatus.BLOCKED);

        if (check != null) {
            if (check.equals("success")) {
                Optional<Follow> optionalBlockedFollow = followRepository.findByFollowerAndFolloweeAndFollowStatus(appUser, followee, FollowStatus.BLOCKED);
                if (optionalBlockedFollow.isEmpty()) {
                    followForErrorDto.setErrorMessage("[SERVER ERROR] - Wrong Server Logic");
                    return followForErrorDto;
                }

                return FollowDto.followToFollowDto(optionalBlockedFollow.get());
            } else {
                followForErrorDto.setErrorMessage(check);
                return followForErrorDto;
            }
        } else {
            Follow follow = new Follow();
            follow.setFollower(appUser);
            follow.setFollowee(followee);
            follow.setFollowStatus(FollowStatus.BLOCKED);
            followRepository.save(follow);

            return FollowDto.followToFollowDto(follow);
        }
    }

    @Override
    @Transactional
    public String unBlockRequest(Authentication authentication, Long followId) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return "AppUser Not Found";

        AppUser appUser = optionalAppUser.get();

        Optional<Follow> optionalFollow = followRepository.findById(followId);

        if (optionalFollow.isEmpty())
            return "Follow Not Found";

        Follow follow = optionalFollow.get();

        if (follow.getFollowStatus() != FollowStatus.BLOCKED || !follow.getFollower().getAppUserId().equals(appUser.getAppUserId()))
            return "Wrong follow Id (Not Blocked)";

        followRepository.delete(follow);

        return "Success UnBlocked";
    }

    @Override
    public List<FollowDto> retrieveBlockedUsers(Authentication authentication) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return List.of();

        AppUser appUser = optionalAppUser.get();

        List<Follow> blockedFollows = followRepository.findByFollowerAndFollowStatus(appUser, FollowStatus.BLOCKED);

        return blockedFollows.stream().map(FollowDto::followToFollowDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FollowDto acceptFollowRequest(Authentication authentication, Long followId) {
        FollowDto followForErrorDto = new FollowDto();
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty()) {
            followForErrorDto.setErrorMessage("AppUser Not Found");
            return followForErrorDto;
        }

        AppUser appUser = optionalAppUser.get();

        Optional<Follow> optionalFollow = followRepository.findById(followId);

        if (optionalFollow.isEmpty()) {
            followForErrorDto.setErrorMessage("Follow Not Found");
            return followForErrorDto;
        }

        Follow follow = optionalFollow.get();

        if (follow.getFollowStatus() != FollowStatus.REQUESTED) {
            followForErrorDto.setErrorMessage("Wrong follow Id (Not Requested)");
            return followForErrorDto;
        }

        if (!follow.getFollowee().getAppUserId().equals(appUser.getAppUserId())) {
            // followee (나) 만 승인 가능
            followForErrorDto.setErrorMessage("Not owner of this follow request");
            return followForErrorDto;
        }

        follow.setFollowStatus(FollowStatus.FOLLOWED);
        followRepository.save(follow);

        AppUser counterParty = follow.getFollower();

        Follow acceptFollow = new Follow();
        acceptFollow.setFollower(appUser);
        acceptFollow.setFollowee(counterParty);
        acceptFollow.setFollowStatus(FollowStatus.FOLLOWED);
        followRepository.save(acceptFollow);

        appUser.getMyProfile().setFollowingCount(appUser.getMyProfile().getFollowingCount() + 1);
        appUserRepository.save(appUser);

        counterParty.getMyProfile().setFollowerCount(counterParty.getMyProfile().getFollowerCount() + 1);
        appUserRepository.save(counterParty);

        return FollowDto.followToFollowDto(acceptFollow);
    }

    @Override
    @Transactional
    public FollowDto declineFollowRequest(Authentication authentication, Long followId) {
        FollowDto followForErrorDto = new FollowDto();
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty()) {
            followForErrorDto.setErrorMessage("AppUser Not Found");
            return followForErrorDto;
        }

        AppUser appUser = optionalAppUser.get();

        Optional<Follow> optionalFollow = followRepository.findById(followId);

        if (optionalFollow.isEmpty()) {
            followForErrorDto.setErrorMessage("Follow Not Found");
            return followForErrorDto;
        }

        Follow follow = optionalFollow.get();

        if (follow.getFollowStatus() != FollowStatus.REQUESTED) {
            followForErrorDto.setErrorMessage("Wrong follow Id (Not Requested)");
            return followForErrorDto;
        }

        if (!follow.getFollowee().getAppUserId().equals(appUser.getAppUserId())) {
            // followee (나) 만 승인 가능
            followForErrorDto.setErrorMessage("Not owner of this follow request");
            return followForErrorDto;
        }

        AppUser counterParty = follow.getFollower();

        followRepository.delete(follow);
        counterParty.getMyProfile().setFollowingCount(counterParty.getMyProfile().getFollowingCount() - 1);
        appUser.getMyProfile().setFollowerCount(appUser.getMyProfile().getFollowerCount() - 1);

        Follow acceptFollow = new Follow();
        acceptFollow.setFollower(appUser);
        acceptFollow.setFollowee(counterParty);
        acceptFollow.setFollowStatus(FollowStatus.DECLINED);
        followRepository.save(acceptFollow);

        return FollowDto.followToFollowDto(acceptFollow);
    }

    @Override
    @Scheduled(fixedRate = SCHEDULE_TIME)
    public void removeExpiredDeclinedFollows() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(DECLINED_TTL_HOURS);
        followRepository.deleteAllWithStatusBefore(FollowStatus.DECLINED, cutoff);
    }

    @Override
    public List<RetrieveProfileDto> retrieveTaBiUser(Authentication authentication, RetrieveProfileRequest retrieveProfileRequest) {
        String keyword = (retrieveProfileRequest != null) ? retrieveProfileRequest.getKeyword() : null;

        if (keyword == null)
            return List.of();

        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return List.of();

        AppUser appUser = optionalAppUser.get();

        String searchPrefix = keyword.trim();

        if (searchPrefix.isEmpty())
            return List.of();

        List<MyProfile> profiles = myProfileRepository.findTop30ByNickNameStartingWithIgnoreCaseAndAppUserNotOrderByNickNameAsc(searchPrefix, appUser);

        return profiles.stream().map(RetrieveProfileDto::createRetrieveProfileDto).toList();
    }

    private void reverseFollow(AppUser follower, AppUser followee) {
        Optional<Follow> optionalRequestFollow = followRepository.findByFollowerAndFolloweeAndFollowStatus(followee, follower, FollowStatus.REQUESTED);

        if (optionalRequestFollow.isPresent()) {
            // 상대방이 이미 요청한 이력이 존재할 경우 업데이트
            Follow follow = optionalRequestFollow.get();
            follow.setFollowStatus(FollowStatus.FOLLOWED);
            followRepository.save(follow);
        } else {
            // 요청한 이력이 없는 경우 새로 생성
            Follow follow = new Follow();
            follow.setFollower(followee);
            follow.setFollowee(follower);
            follow.setFollowStatus(FollowStatus.FOLLOWED);
            followRepository.save(follow);

            followee.getMyProfile().setFollowingCount(followee.getMyProfile().getFollowingCount() + 1);
            follower.getMyProfile().setFollowerCount(follower.getMyProfile().getFollowerCount() + 1);
        }
    }

    private String checkFollowStatus(AppUser appUser, AppUser followee, FollowStatus followStatus) {
        Optional<Follow> optionalCounterpartyBlockedFollow = followRepository.findByFollowerAndFolloweeAndFollowStatus(followee, appUser, FollowStatus.BLOCKED);
        Optional<Follow> optionalCounterpartyDeclinedFollow = followRepository.findByFollowerAndFolloweeAndFollowStatus(followee, appUser, FollowStatus.DECLINED);
        Optional<Follow> optionalCounterpartyRequestFollow = followRepository.findByFollowerAndFolloweeAndFollowStatus(followee, appUser, FollowStatus.REQUESTED);
        Optional<Follow> optionalCounterpartyFollowedFollow = followRepository.findByFollowerAndFolloweeAndFollowStatus(followee, appUser, FollowStatus.FOLLOWED);

        Optional<Follow> optionalFollowedFollow = followRepository.findByFollowerAndFolloweeAndFollowStatus(appUser, followee, FollowStatus.FOLLOWED);
        Optional<Follow> optionalRequestFollow = followRepository.findByFollowerAndFolloweeAndFollowStatus(appUser, followee, FollowStatus.REQUESTED);
        Optional<Follow> optionalBlockedFollow = followRepository.findByFollowerAndFolloweeAndFollowStatus(appUser, followee, FollowStatus.BLOCKED);
        Optional<Follow> optionalDeclinedFollow = followRepository.findByFollowerAndFolloweeAndFollowStatus(appUser, followee, FollowStatus.DECLINED);


        switch (followStatus) {
            case REQUESTED -> {
                if (optionalCounterpartyBlockedFollow.isPresent() || optionalCounterpartyDeclinedFollow.isPresent())
                    // 차단 혹은 거절 이력이 있는 경우
                    return "Unable to request (BLOCK or DECLINED)";

                if (optionalFollowedFollow.isPresent())
                    // 이미 팔로우가 되어있는 경우
                    return "Already Followed";

                if (optionalRequestFollow.isPresent())
                    // 이미 요청을 건 상태인 경우
                    return "Already Requested";

                if (optionalBlockedFollow.isPresent())
                    // 내가 이미 차단을 건 경우
                    return "I blocked the other person, please unblock me first";

                return null;
            }
            case FOLLOWED -> {
                return null;
            }
            case BLOCKED -> {
                if (optionalBlockedFollow.isPresent())
                    return "Already Blocked";

                if (optionalCounterpartyRequestFollow.isPresent()) {
                    Follow counterpartyRequestedEdge = optionalCounterpartyRequestFollow.get();
                    followRepository.delete(counterpartyRequestedEdge);

                    followee.getMyProfile().setFollowingCount(followee.getMyProfile().getFollowingCount() - 1); // B following--
                    appUser.getMyProfile().setFollowerCount(appUser.getMyProfile().getFollowerCount() - 1);     // A follower--

                    Follow myBlockedEdge = new Follow();
                    myBlockedEdge.setFollower(appUser);   // A
                    myBlockedEdge.setFollowee(followee);  // B
                    myBlockedEdge.setFollowStatus(FollowStatus.BLOCKED);
                    followRepository.save(myBlockedEdge);

                    return "success";
                }

                if (optionalRequestFollow.isPresent()) {
                    Follow myRequestedEdge = optionalRequestFollow.get();
                    // 요청 → 차단 전환
                    myRequestedEdge.setFollowStatus(FollowStatus.BLOCKED);
                    followRepository.save(myRequestedEdge);

                    appUser.getMyProfile().setFollowingCount(appUser.getMyProfile().getFollowingCount() - 1);   // A following--
                    followee.getMyProfile().setFollowerCount(followee.getMyProfile().getFollowerCount() - 1);   // B follower--

                    return "success";
                }

                if (optionalDeclinedFollow.isPresent()) {
                    Follow follow = optionalDeclinedFollow.get();
                    follow.setFollowStatus(FollowStatus.BLOCKED);
                    followRepository.save(follow);

                    return "success";
                }

                if (optionalFollowedFollow.isPresent()) {
                    if (optionalCounterpartyFollowedFollow.isEmpty())
                        return "[SERVER ERROR] - Wrong Followed Match";

                    // 상대방 팔로우 해제
                    Follow counterpartyFollowedEdge = optionalCounterpartyFollowedFollow.get();
                    followRepository.delete(counterpartyFollowedEdge);

                    followee.getMyProfile().setFollowingCount(followee.getMyProfile().getFollowingCount() - 1); // B following--
                    appUser.getMyProfile().setFollowerCount(appUser.getMyProfile().getFollowerCount() - 1);     // A follower--

                    Follow myFollowedEdge = optionalFollowedFollow.get();
                    myFollowedEdge.setFollowStatus(FollowStatus.BLOCKED);
                    followRepository.save(myFollowedEdge);

                    appUser.getMyProfile().setFollowingCount(appUser.getMyProfile().getFollowingCount() - 1);   // A following--
                    followee.getMyProfile().setFollowerCount(followee.getMyProfile().getFollowerCount() - 1);   // B follower--

                    return "success";
                }

                return null;
            }
            case DECLINED -> {
                return null;
            }
            default -> {
                return "[SERVER ERROR] - Wrong Follow Status";
            }
        }
    }
}
