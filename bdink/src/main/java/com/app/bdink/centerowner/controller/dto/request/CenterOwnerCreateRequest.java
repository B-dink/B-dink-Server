package com.app.bdink.centerowner.controller.dto.request;

/**
 * 센터장 생성 요청 DTO.
 * 멤버 정보는 토큰(Principal) 기준으로 결정한다.
 */
public record CenterOwnerCreateRequest(
        Long centerId
) {
}
