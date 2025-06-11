package com.study.deployment.service.aws;

import com.study.deployment.config.AwsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
@RequiredArgsConstructor
public class SqsService {

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
