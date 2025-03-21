package com.app.bdink.external.aws.s3.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    private static final String AWS_ACCESS_KEY_ID = "aws.accessKeyId";
    private static final String AWS_SECRET_ACCESS_KEY = "aws.secretAccessKey";

    @Getter
    private final String videoDir = "video";

    private final String accessKey;
    private final String secretKey;
    private final String regionString;

    @Getter
    private final String s3Bucket;

    public S3Config(@Value("${aws-property.access-key}") final String accessKey,
                    @Value("${aws-property.secret-key}") final String secretKey,
                    @Value("${aws-property.aws-region}") final String regionString,
                    @Value("${aws-property.s3-bucket-name}") final String s3Bucket) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.regionString = regionString;
        this.s3Bucket = s3Bucket;
    }

    @Bean
    public AwsCredentials basicAWSCredentials(){
        return AwsBasicCredentials.create(accessKey,secretKey);
    }


    @Bean
    public SystemPropertyCredentialsProvider systemPropertyCredentialsProvider() {
        System.setProperty(AWS_ACCESS_KEY_ID, accessKey);
        System.setProperty(AWS_SECRET_ACCESS_KEY, secretKey);
        return SystemPropertyCredentialsProvider.create();
    }

    @Bean
    public S3Presigner s3Presigner(){
        return S3Presigner.builder()
                .region(Region.of(regionString))
                .credentialsProvider(systemPropertyCredentialsProvider())
                .build();
    }

    @Bean
    public Region getRegion() {
        return Region.of(regionString);
    }

    @Bean
    public S3Client getS3Client() {
        return S3Client.builder()
                .region(getRegion())
                .credentialsProvider(systemPropertyCredentialsProvider())
                .build();
    }

}
