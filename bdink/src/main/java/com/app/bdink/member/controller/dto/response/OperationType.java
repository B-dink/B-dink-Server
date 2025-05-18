package com.app.bdink.member.controller.dto.response;

import lombok.Getter;

@Getter
public enum OperationType {
    PHONE_UPDATE("폰번호 업데이트"),
    MARKETING_UPDATE("마케팅 수신 설정 업데이트"),
    NAME_UPDATE("이름 업데이트"),
    PROFILE_PICTURE_UPDATE("프로필 사진 업데이트");

    private final String description;

    OperationType(String description) {
        this.description = description;
    }
}