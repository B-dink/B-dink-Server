package com.app.bdink.centerowner.controller.dto.request;

/**
 * 센터장 수정 요청 DTO.
 * 센터 변경이 필요할 때 사용한다.
 */
public record CenterOwnerUpdateRequest(
        Long centerId
) {
}
