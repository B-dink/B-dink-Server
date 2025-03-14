package com.app.bdink.external.coolsms.controller;

import com.app.bdink.external.coolsms.dto.request.SmsSendRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/sms")
@RequiredArgsConstructor
public class SmsController {

    private DefaultMessageService messageService;

    @Value("${coolsms.API_KEY}")
    private String API_KEY;

    @Value("${coolsms.API_SECRET_KEY}")
    private String API_SECRET_KEY;

    @Value("${coolsms.PHONE}")
    private String BDINK_PHONE;

    @PostConstruct
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(API_KEY, API_SECRET_KEY, "https://api.coolsms.co.kr");
    }

    @PostMapping("/send-one")
    public SingleMessageSentResponse sendOne(@RequestBody SmsSendRequest smsRequest) {
        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        // random 6자리 숫자 발송.
        message.setFrom(BDINK_PHONE);
        message.setTo(smsRequest.phone());
        message.setText("버딩크 문자 인증 코드입니다. \n" + smsRequest.generateRandomCode());

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        log.info(response.toString());

        return response;
    }

}
