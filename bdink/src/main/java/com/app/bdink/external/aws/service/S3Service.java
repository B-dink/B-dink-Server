package com.app.bdink.external.aws.service;

import com.app.bdink.external.aws.S3Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class S3Service {

    private final String bucketName;
    private final S3Config s3Config;

    public S3Service(@Value("${aws-property.s3-bucket-name}") final String bucketName, S3Config s3Config) {
        this.bucketName = bucketName;
        this.s3Config = s3Config;
    }


    public String uploadImage(String directoryPath, MultipartFile image) {
        final String key = directoryPath + generateImageFileName();
        final S3Client s3Client = s3Config.getS3Client();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(image.getContentType())
                .contentDisposition("inline")
                .build();

        try{
            RequestBody requestBody = RequestBody.fromBytes(image.getBytes());
            s3Client.putObject(request, requestBody);
        }catch (IOException e){
            log.info("이미지 저장 중 에러발생.");
        }
        return key;
    }

    public String uploadMedia(String directoryPath, MultipartFile media) {
        final String key = directoryPath + generateMediaFileName();
        final S3Client s3Client = s3Config.getS3Client();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(media.getContentType())
                .contentDisposition("inline")
                .build();

        try{
            RequestBody requestBody = RequestBody.fromBytes(media.getBytes());
            s3Client.putObject(request, requestBody);
        }catch (IOException e){
            log.info("이미지 저장 중 에러발생.");
        }
        return key;
    }

    public void deleteImageAndMedia(String key) {
        final S3Client s3Client = s3Config.getS3Client();
        s3Client.deleteObject((DeleteObjectRequest.Builder builder) ->
                builder.bucket(bucketName)
                        .key(key)
                        .build()
        );
    }


    private String generateImageFileName() {
        return UUID.randomUUID().toString() + ".jpg";
    }

    private String generateMediaFileName() {
        return UUID.randomUUID().toString() + ".m3u8";
    }

    public String generateOriginalLink(String imageKey){
        return "https://"+bucketName+".s3."+ s3Config.getRegion()+".amazonaws.com/"+imageKey;
    }

}
