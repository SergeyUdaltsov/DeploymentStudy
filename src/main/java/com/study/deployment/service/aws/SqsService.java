package com.study.deployment.service.aws;

import com.study.deployment.config.AwsProperties;
import com.study.deployment.service.MessageProcessingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
@RequiredArgsConstructor
public class SqsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SqsService.class);


    private final SqsClient sqsClient;
    private final AwsProperties awsProperties;

    public void sendMessage(String messageBody) {
        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(awsProperties.getSqs().getQueueUrl())
                .messageBody(messageBody)
                .build();

        sqsClient.sendMessage(request);
    }
}
