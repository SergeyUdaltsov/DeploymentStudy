package com.study.deployment.service.aws;

import com.study.deployment.config.AwsProperties;
import com.study.deployment.service.SqsMessageListener;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.List;

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

    public List<Message> getMessagesFromQueue() {
        ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                .queueUrl(awsProperties.getSqs().getQueueUrl())
                .maxNumberOfMessages(5)
                .waitTimeSeconds(10)
                .build();

       return sqsClient.receiveMessage(receiveRequest).messages();
    }

    public void deleteMessage(Message message) {
        sqsClient.deleteMessage(DeleteMessageRequest.builder()
                .queueUrl(awsProperties.getSqs().getQueueUrl())
                .receiptHandle(message.receiptHandle())
                .build());

        LOGGER.info("Message with id: {} was deleted from the queue", message.messageId());
    }
}
