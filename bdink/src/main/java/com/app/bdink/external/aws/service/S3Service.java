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
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class S3Service {

    private final String bucketName;
    private final S3Config s3Config;
    private final String cdnName;

    private final String cdnMedia;

    public S3Service(@Value("${aws-property.s3-bucket-name}") final String bucketName,
                     S3Config s3Config,
                     @Value("${aws-property.cdn-name}") final String cdnName,
                     @Value("${aws-property.cdn-media}") final String cdnMedia) {
        this.bucketName = bucketName;
        this.s3Config = s3Config;
        this.cdnName = cdnName;
        this.cdnMedia = cdnMedia;
    }


    public String uploadImageOrMedia(String directoryPath, MultipartFile image) {
        String key = null;
        // TODO: 나중에 enum으로 분기.
        if (Objects.equals(directoryPath, "image/")){
            key = directoryPath + generateImageFileName();
        } else if (Objects.equals(directoryPath, "media/")) {
            key = directoryPath + generateMediaFileName();
        }

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
        if (Objects.equals(directoryPath, "image/")){
            return generateCdnImageKey(key);
        } else if (Objects.equals(directoryPath, "media/")) {
            return generateCdnLink(key);
        }
        return null;
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

    private String generateCdnImageKey(String key){
        return cdnName+key;
    }

    private String generateMediaFileName() {
        return UUID.randomUUID().toString() + ".mp4";
    }

    public String generateOriginalLink(String imageKey){
        return "https://"+bucketName+".s3."+ s3Config.getRegion()+".amazonaws.com/"+imageKey;
    }

    public String generateCdnLink(String mediaKey){
        String originalName = mediaKey.substring(6,mediaKey.length()-4);
        return cdnMedia+originalName+"/HLS/"+originalName+"_360.m3u8";
    }

}
