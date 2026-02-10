package com.app.bdink.notification.controller;

import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.member.util.MemberUtilService;
import com.app.bdink.notification.controller.dto.DeviceTokenAllowedRequest;
import com.app.bdink.notification.controller.dto.DeviceTokenRegisterRequest;
import com.app.bdink.notification.controller.dto.NotificationResponse;
import com.app.bdink.notification.service.DeviceTokenService;
import com.app.bdink.notification.service.NotificationService;
import com.app.bdink.notification.service.PushNotificationService;
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
    private final PushNotificationService pushNotificationService;
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

    @DeleteMapping("/{notificationId}")
    @Operation(method = "DELETE", description = "특정 알림을 삭제(soft delete)합니다.")
    public RspTemplate<?> deleteNotification(Principal principal, @PathVariable Long notificationId) {
        Long memberId = memberUtilService.getMemberId(principal);
        notificationService.softDeleteNotification(memberId, notificationId);
        return RspTemplate.success(Success.DELETE_NOTIFICATION_SUCCESS, Success.DELETE_NOTIFICATION_SUCCESS.getMessage());
    }

    @PostMapping("/tokens")
    @Operation(method = "POST", description = "FCM 디바이스 토큰을 등록/갱신합니다.")
    public RspTemplate<?> registerToken(Principal principal,
                                        @RequestBody DeviceTokenRegisterRequest request) {
        Long memberId = memberUtilService.getMemberId(principal);
        deviceTokenService.registerOrUpdate(memberId, request.token(), request.platform(), request.isAllowed());
        return RspTemplate.success(Success.REGISTER_DEVICE_TOKEN_SUCCESS, Success.REGISTER_DEVICE_TOKEN_SUCCESS.getMessage());
    }

    @PutMapping("/tokens/allowed")
    @Operation(method = "PUT", description = "FCM 디바이스 토큰 알림 권한을 변경합니다.")
    public RspTemplate<?> updateAllowed(Principal principal,
                                        @RequestBody DeviceTokenAllowedRequest request) {
        Long memberId = memberUtilService.getMemberId(principal);
        deviceTokenService.updateAllowed(memberId, request.token(), request.isAllowed());
        return RspTemplate.success(Success.UPDATE_DEVICE_TOKEN_ALLOWED_SUCCESS, Success.UPDATE_DEVICE_TOKEN_ALLOWED_SUCCESS.getMessage());
    }

    @GetMapping("/tokens/allowed/verify")
    @Operation(method = "GET", description = "FCM 디바이스 토큰 알림 권한을 확인합니다.")
    public RspTemplate<?> verifyAllowed(Principal principal, @RequestParam String token) {
        Long memberId = memberUtilService.getMemberId(principal);
        Boolean isAllowed = deviceTokenService.verifyForMember(memberId, token);
        return RspTemplate.success(Success.VERIFY_DEVICE_TOKEN_ALOOWED_SUCCESS, isAllowed);
    }

    @GetMapping("/unread")
    @Operation(method = "GET", description = "읽지 않은 알림이 있는지 확인합니다.")
    public RspTemplate<?> checkUnreadNotification(Principal principal) {
        Long memberId = memberUtilService.getMemberId(principal);
        Boolean hasUnread =  notificationService.hasUnread(memberId);
        return RspTemplate.success(Success.GET_NOTIFICATION_READ, hasUnread);
    }

    @DeleteMapping("/tokens")
    @Operation(method = "DELETE", description = "FCM 디바이스 토큰을 비활성화합니다.")
    public RspTemplate<?> deactivateToken(Principal principal,
                                          @RequestParam String token) {
        Long memberId = memberUtilService.getMemberId(principal);
        deviceTokenService.deactivate(memberId, token);
        return RspTemplate.success(Success.DEACTIVATE_DEVICE_TOKEN_SUCCESS, Success.DEACTIVATE_DEVICE_TOKEN_SUCCESS.getMessage());
    }

    @PostMapping("/fcmtest")
    @Operation(method = "POST", description = "FCM 테스트 알림을 전송합니다.")
    public RspTemplate<?> sendFcmTest(Principal principal) {
        Long memberId = memberUtilService.getMemberId(principal);
        pushNotificationService.sendToMember(memberId, "FCM 테스트", "테스트 메시지입니다.", "TEST", "");
        return RspTemplate.success(Success.FCM_TEST_SUCCESS, Success.FCM_TEST_SUCCESS.getMessage());
    }
}
