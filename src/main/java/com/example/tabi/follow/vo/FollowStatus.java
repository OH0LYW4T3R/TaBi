package com.example.tabi.follow.vo;

public enum FollowStatus {
    REQUESTED,   // 팔로우 요청 생성(승인 대기)
    FOLLOWED,    // 승인됨(팔로워 관계 형성)
    DECLINED,    // 거절됨
    BLOCKED      // 차단
}
