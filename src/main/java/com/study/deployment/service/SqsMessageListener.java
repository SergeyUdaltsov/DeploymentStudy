package com.study.deployment.service;

import com.study.deployment.service.aws.DynamoDbService;
import com.study.deployment.service.aws.S3Service;
import com.study.deployment.service.aws.SqsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SqsMessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqsMessageListener.class);

    private final SqsService sqsService;
    private final DynamoDbService dynamoDbService;
    private final S3Service s3Service;

    @Scheduled(fixedDelay = 500)
    public void pollMessages() {
        List<Message> messages = sqsService.getMessagesFromQueue();

        for (Message message : messages) {
            String itemId = message.body();

            try {
                String fileKey = dynamoDbService.getFileMetadata(itemId);
                LOGGER.info("Got file key from db: {}", fileKey);

                String fileContent = s3Service.getSFilePayload(fileKey);
                LOGGER.info("Successfully processed message: {}", fileContent);

            } catch (Exception e) {
                LOGGER.warn(e.getMessage());
            }

            sqsService.deleteMessage(message);
        }
    }
}
