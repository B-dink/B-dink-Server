package com.app.bdink.trainermember.entity;

/**
 * 트레이너-멤버 소속의 활성/비활성 상태를 정의한다.
 * soft delete는 INACTIVE로 처리한다.
 */
public enum TrainerMemberStatus {
    ACTIVE,
    INACTIVE
}
