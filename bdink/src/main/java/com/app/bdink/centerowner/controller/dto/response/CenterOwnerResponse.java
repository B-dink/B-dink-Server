package com.app.bdink.centerowner.controller.dto.response;

import com.app.bdink.centerowner.entity.CenterOwner;
import com.app.bdink.centerowner.entity.CenterOwnerStatus;

/**
 * 센터장 조회 응답 DTO.
 */
public record CenterOwnerResponse(
        Long id,
        Long centerId,
        Long memberId,
        CenterOwnerStatus status
) {
    public static CenterOwnerResponse from(CenterOwner centerOwner) {
        return new CenterOwnerResponse(
                centerOwner.getId(),
                centerOwner.getCenter().getId(),
                centerOwner.getMember().getId(),
                centerOwner.getStatus()
        );
    }
}
