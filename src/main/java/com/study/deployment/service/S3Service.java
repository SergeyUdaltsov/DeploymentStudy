package com.study.deployment.service;

import com.study.deployment.config.AwsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
@RequiredArgsConstructor
public class S3Service {

    private final S3Client client;
    private final AwsProperties awsProperties;

    public void savePayloadToBucket(String payload, String fileKey) {
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(awsProperties.getS3().getBucketName())
                .key(fileKey)
                .build();

        client.putObject(putRequest, RequestBody.fromString(payload));
    }
}
