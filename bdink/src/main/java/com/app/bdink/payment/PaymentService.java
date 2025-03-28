package com.app.bdink.payment;

import com.app.bdink.payment.domain.PaymentConfirmResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private String WIDGET_SECRET = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6"; //TODO: 테스트에서 나중에 사업자등록된거로 받기.
    private final String tossUrl = "https://api.tosspayments.com/v1/payments";

    public void confirm(){
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((WIDGET_SECRET + ":").getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);
        RestClient restClient = RestClient.create(tossUrl);
        restClient.post()
                .uri("/confirm")
                .header("Authorization", authorizations)
                .header("Content-Type", "application/json")
                .retrieve()
                .toEntity(PaymentConfirmResponse.class);
    }

}
