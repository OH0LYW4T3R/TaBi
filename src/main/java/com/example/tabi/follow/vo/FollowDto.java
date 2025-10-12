package com.example.tabi.follow.vo;

import com.example.tabi.follow.entity.Follow;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowDto {
    private Long followId;

    private Long followerId;
    private String followerNickName;
    private Long followeeId;
    private String followeeNickName;

    private FollowStatus followStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String errorMessage;

    public static FollowDto followToFollowDto(Follow follow) {
        return new  FollowDto(
                follow.getFollowId(),
                follow.getFollower().getAppUserId(),
                follow.getFollower().getMyProfile().getNickName(),
                follow.getFollowee().getAppUserId(),
                follow.getFollowee().getMyProfile().getNickName(),
                follow.getFollowStatus(),
                follow.getCreatedAt(),
                follow.getUpdatedAt(),
                null
        );
    }
}
