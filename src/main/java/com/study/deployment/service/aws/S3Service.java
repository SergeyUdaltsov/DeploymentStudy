package com.study.deployment.service.aws;

import com.study.deployment.config.AwsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class S3Service {

    private final S3Client client;
    private final AwsProperties awsProperties;

    public void saveFileToS3(String payload, String fileKey) {
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(awsProperties.getS3().getBucketName())
                .key(fileKey)
                .build();

        client.putObject(putRequest, RequestBody.fromString(payload));
    }

    public String getSFilePayload(String fileKey) {
        String bucketName = awsProperties.getS3().getBucketName();

        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build();

            ResponseBytes<GetObjectResponse> objectBytes = client.getObjectAsBytes(request);

            return objectBytes.asString(StandardCharsets.UTF_8);
        } catch (S3Exception e) {
            throw new RuntimeException(String.format("Failed to get file %s from bucket %s", fileKey, bucketName));
        }
    }
}
