package com.study.deployment.service;

import com.study.deployment.config.AwsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class S3Service {

    private final S3Client client;
    private final AwsProperties awsProperties;

    public void savePayloadToBucket(String payload) {
        LocalDateTime now = LocalDateTime.now();
        String key = String.format("%s/%s/%s/%s", now.getYear(), now.getMonth().getValue(), now.getDayOfMonth(), Timestamp.valueOf(now));

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(awsProperties.getS3().getBucketName())
                .key(key)
                .build();

        client.putObject(putRequest, RequestBody.fromString(payload));
    }
}
