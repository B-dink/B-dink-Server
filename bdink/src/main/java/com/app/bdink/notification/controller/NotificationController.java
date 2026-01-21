package com.app.bdink.notification.controller;

import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.member.util.MemberUtilService;
import com.app.bdink.notification.controller.dto.DeviceTokenAllowedRequest;
import com.app.bdink.notification.controller.dto.DeviceTokenRegisterRequest;
import com.app.bdink.notification.controller.dto.NotificationResponse;
import com.app.bdink.notification.service.DeviceTokenService;
import com.app.bdink.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
@Tag(name = "알림 API", description = "인앱 알림 조회/읽음 처리 API입니다.")
public class NotificationController {

    private final NotificationService notificationService;
    private final DeviceTokenService deviceTokenService;
    private final MemberUtilService memberUtilService;

    @GetMapping
    @Operation(method = "GET", description = "내 알림 목록을 조회합니다.")
    public RspTemplate<List<NotificationResponse>> getNotifications(Principal principal) {
        Long memberId = memberUtilService.getMemberId(principal);
        List<NotificationResponse> responses = notificationService.getNotifications(memberId);
        return RspTemplate.success(Success.GET_NOTIFICATION_SUCCESS, responses);
    }

    @PutMapping("/{notificationId}/read")
    @Operation(method = "PUT", description = "특정 알림을 읽음 처리합니다.")
    public RspTemplate<?> markAsRead(Principal principal, @PathVariable Long notificationId) {
        Long memberId = memberUtilService.getMemberId(principal);
        notificationService.markAsRead(memberId, notificationId);
        return RspTemplate.success(Success.UPDATE_NOTIFICATION_SUCCESS, Success.UPDATE_NOTIFICATION_SUCCESS.getMessage());
    }

    @PostMapping("/tokens")
    @Operation(method = "POST", description = "FCM 디바이스 토큰을 등록/갱신합니다.")
    public RspTemplate<?> registerToken(Principal principal,
                                        @RequestBody DeviceTokenRegisterRequest request) {
        Long memberId = memberUtilService.getMemberId(principal);
        deviceTokenService.registerOrUpdate(memberId, request.token(), request.platform(), request.isAllowed());
        return RspTemplate.success(Success.UPDATE_NOTIFICATION_SUCCESS, Success.UPDATE_NOTIFICATION_SUCCESS.getMessage());
    }

    @PutMapping("/tokens/allowed")
    @Operation(method = "PUT", description = "FCM 디바이스 토큰 알림 권한을 변경합니다.")
    public RspTemplate<?> updateAllowed(Principal principal,
                                        @RequestBody DeviceTokenAllowedRequest request) {
        Long memberId = memberUtilService.getMemberId(principal);
        deviceTokenService.updateAllowed(memberId, request.token(), request.isAllowed());
        return RspTemplate.success(Success.UPDATE_NOTIFICATION_SUCCESS, Success.UPDATE_NOTIFICATION_SUCCESS.getMessage());
    }

    @DeleteMapping("/tokens")
    @Operation(method = "DELETE", description = "FCM 디바이스 토큰을 비활성화합니다.")
    public RspTemplate<?> deactivateToken(Principal principal,
                                          @RequestParam String token) {
        Long memberId = memberUtilService.getMemberId(principal);
        deviceTokenService.deactivate(memberId, token);
        return RspTemplate.success(Success.UPDATE_NOTIFICATION_SUCCESS, Success.UPDATE_NOTIFICATION_SUCCESS.getMessage());
    }
}
