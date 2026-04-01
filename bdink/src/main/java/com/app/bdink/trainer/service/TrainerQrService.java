package com.app.bdink.trainer.service;

import com.app.bdink.external.aws.service.S3Service;
import com.app.bdink.trainer.entity.Trainer;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainerQrService {

    private static final String TRAINER_QR_DIRECTORY = "image/trainer-qr/";
    private static final int QR_WIDTH = 300;
    private static final int QR_HEIGHT = 300;

    private final S3Service s3Service;

    @Transactional
    public void ensureTrainerQr(Trainer trainer) {
        // 결제 시점에 최초 1회만 생성하고, 이미 있으면 기존 QR을 재사용한다.
        if (trainer.getQrToken() != null && !trainer.getQrToken().isBlank()) {
            return;
        }

        String qrToken = generateQrToken();
        byte[] qrImageBytes = generateQrImage(qrToken);
        String fileName = buildQrFileName(trainer.getId());
        String qrImageUrl = s3Service.uploadImageBytes(TRAINER_QR_DIRECTORY, fileName, qrImageBytes, "image/png");

        trainer.updateQrInfo(qrToken, qrImageUrl);
    }

    private String generateQrToken() {
        return UUID.randomUUID().toString();
    }

    private String buildQrFileName(Long trainerId) {
        return "trainer-" + trainerId + "-" + UUID.randomUUID() + ".png";
    }

    private byte[] generateQrImage(String qrPayload) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrPayload, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            return outputStream.toByteArray();
        } catch (WriterException | IOException e) {
            throw new IllegalStateException("QR 이미지 생성에 실패했습니다.", e);
        }
    }
}
