package com.example.tabi.follow.service;

import com.example.tabi.follow.vo.FollowDto;
import com.example.tabi.follow.vo.RetrieveProfileDto;
import com.example.tabi.follow.vo.RetrieveProfileRequest;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface FollowService {
    FollowDto followRequest(Authentication authentication, Long followeeId);
    String followCancel(Authentication authentication, Long followeeId);
    List<RetrieveProfileDto> retrieveMyFollowers(Authentication authentication);
    List<RetrieveProfileDto> retrieveMyFollowings(Authentication authentication);
    FollowDto blockRequest(Authentication authentication, Long followeeId);
    String unBlockRequest(Authentication authentication, Long followId);
    List<FollowDto> retrieveBlockedUsers(Authentication authentication);
    FollowDto acceptFollowRequest(Authentication authentication, Long followId);
    FollowDto declineFollowRequest(Authentication authentication, Long followId);
    void removeExpiredDeclinedFollows();
    List<RetrieveProfileDto> retrieveTaBiUser(Authentication authentication, RetrieveProfileRequest retrieveProfileRequest);
}
