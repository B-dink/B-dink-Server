package com.app.bdink.centerowner.entity;

/**
 * 센터장의 활성/비활성 상태를 정의한다.
 * soft delete는 INACTIVE로 처리한다.
 */
public enum CenterOwnerStatus {
    ACTIVE,
    INACTIVE
}
