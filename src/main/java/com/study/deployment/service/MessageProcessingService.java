package com.study.deployment.service;

import com.study.deployment.service.aws.DynamoDbService;
import com.study.deployment.service.aws.S3Service;
import com.study.deployment.service.aws.SqsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageProcessingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessingService.class);

    @Value("env-dependent-value")
    private String envDependentValue;
    private final S3Service s3Service;
    private final DynamoDbService dynamoDbService;
    private final SqsService sqsService;
    private final DescribeEnvProcessor describeEnvProcessor;

    public void processMessage(String message) {
        LOGGER.info("Start processing message by env: {}", describeEnvProcessor.describeEnv());
        LOGGER.info("Env dependent value: {}", envDependentValue);

        LocalDateTime now = LocalDateTime.now();
        String fileKey = String.format("%s/%s/%s/%s", now.getYear(), now.getMonth().getValue(), now.getDayOfMonth(), Timestamp.valueOf(now));

        s3Service.saveFileToS3(message, fileKey);
        LOGGER.info("Payload successfully stored to S3");

        UUID metadataId = UUID.randomUUID();
        dynamoDbService.saveFileMetadata(metadataId.toString(), fileKey);
        LOGGER.info("Payload metadata successfully persisted to DynamoDb");

        sqsService.sendMessage(metadataId.toString());
        LOGGER.info("Payload metadata ID successfully pushed to SQS");
    }
}
