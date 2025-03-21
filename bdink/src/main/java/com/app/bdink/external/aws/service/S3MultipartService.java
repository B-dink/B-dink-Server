package com.app.bdink.external.aws.service;

import com.app.bdink.external.aws.s3.config.S3Config;
import com.app.bdink.external.aws.s3.controller.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedUploadPartRequest;
import software.amazon.awssdk.services.s3.presigner.model.UploadPartPresignRequest;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3MultipartService {

    private final S3Client s3Client;
    private final S3Config s3Config;
    private final S3Presigner s3Presigner;

    public S3UploadDto initiateUpload(String originalFileName){
        String targetBucket = s3Config.getS3Bucket();
        String targetObjectDir = s3Config.getVideoDir();



        //사용자가 보낸 파일 확장자와 현재 시간을 이용해서 새로운 파일이름을 만든다.
        String fileType = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
        String newFileName = System.currentTimeMillis() + fileType;
        Instant now = Instant.now();

        CreateMultipartUploadRequest createMultipartUploadRequest =
                CreateMultipartUploadRequest.builder()
                        .bucket(targetBucket)
                        .key(targetObjectDir + "/" + newFileName)
                        .acl(ObjectCannedACL.PUBLIC_READ)
                        .expires(now.plusSeconds(60 * 25)) //객체를 더이상 캐시할 수 없는 날짜 및 시간
                        .build();
        // 고유식별자인 업로드 ID 응답을 반환받기.
        CreateMultipartUploadResponse response = s3Client.createMultipartUpload(createMultipartUploadRequest);
        return new S3UploadDto(response.uploadId(), newFileName);
    }

    public S3PreSignedUrlDto getUploadSignedUrl(S3UploadSignedUrlDto s3UploadSignedUrlDto){
        String targetBucket = s3Config.getS3Bucket();
        String targetObjectDir = s3Config.getVideoDir();

        UploadPartRequest uploadPartRequest = UploadPartRequest.builder()
                .bucket(targetBucket)
                .key(targetObjectDir+"/"+s3UploadSignedUrlDto.fileName())
                .uploadId(s3UploadSignedUrlDto.uploadId())
                .partNumber(s3UploadSignedUrlDto.partNumber())
                .build();

        UploadPartPresignRequest preSignedUploadPartRequestForAws = UploadPartPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .uploadPartRequest(uploadPartRequest)
                .build();

        PresignedUploadPartRequest preSignedUploadPartRequest = s3Presigner.presignUploadPart(preSignedUploadPartRequestForAws);
        return new S3PreSignedUrlDto(preSignedUploadPartRequest.url().toString());
    }

    public S3UploadResultDto completeUpload(S3UploadCompleteDto s3UploadCompleteDto){
        List<CompletedPart> completedPartList = new ArrayList<>();
        String targetBucket = s3Config.getS3Bucket();
        String targetObjectDir = "media";

        //모든 한 영상에 대한 모든 부분들에 부분 번호와 Etag를 설정.
        for (S3UploadPartsDetailDto partForm : s3UploadCompleteDto.parts()){
            CompletedPart part = CompletedPart.builder()
                    .partNumber(partForm.partNumber())
                    .eTag(partForm.AWSEtag())
                    .build();
        }

        //멀티파트 업로드 완료 요청 보내기.
        CompletedMultipartUpload completedMultipartUpload = CompletedMultipartUpload.builder()
                .parts(completedPartList)
                .build();

        String fileName = s3UploadCompleteDto.fileName();
        CompleteMultipartUploadRequest request = CompleteMultipartUploadRequest.builder()
                .bucket(targetBucket)
                .key(targetObjectDir + "/" + fileName)
                .uploadId(s3UploadCompleteDto.uploadId())
                .multipartUpload(completedMultipartUpload)
                .build();
        //합침요청.
        CompleteMultipartUploadResponse response = s3Client.completeMultipartUpload(request);

        String objectKey = response.key();
        String url = response.location(); //TODO: 업로드된 URL 정보.
        String bucket = response.bucket();

        return S3UploadResultDto.builder()
                .name(fileName)
                .url(url)
                .build();
    }


}
