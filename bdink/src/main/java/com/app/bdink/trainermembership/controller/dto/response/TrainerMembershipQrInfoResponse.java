package com.app.bdink.trainermembership.controller.dto.response;

import com.app.bdink.trainermembership.entity.TrainerMembership;
import com.app.bdink.trainer.entity.Trainer;

import java.time.LocalDate;

public record TrainerMembershipQrInfoResponse(
        String qrImageUrl
//        LocalDate expiredDate
) {
    public static TrainerMembershipQrInfoResponse from(Trainer trainer) {
        return new TrainerMembershipQrInfoResponse(
                trainer.getQrImageUrl()
//                membership.getExpiredDate() // 현재 멤버십 적용 x로 기획 변경 후 추 후 다시 사용 예정

        );
    }
}
