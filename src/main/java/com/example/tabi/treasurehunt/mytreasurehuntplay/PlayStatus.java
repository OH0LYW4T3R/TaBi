package com.example.tabi.treasurehunt.mytreasurehuntplay;

// available -> 실행가능한 상태 (실행 가능지역 입장시)
// playing -> 실행한 상태
// pending -> 실행했다가 목적지와 매우 멀어지거나, 몇일동안(3일) 플레이 하지 않은 상태
// cleared -> 내가 보물을 찾은 상태
// terminated -> 댜른 사용자가 이미 찾아서 종료가 된 상태
public enum PlayStatus {
    AVAILABLE,
    PLAYING,
    PENDING,
    CLEARED,
}
