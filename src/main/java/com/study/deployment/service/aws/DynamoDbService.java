package com.study.deployment.service.aws;

import com.study.deployment.config.AwsProperties;
import com.study.deployment.service.MessageProcessingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DynamoDbService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamoDbService.class);

    private final DynamoDbClient dynamoDbClient;
    private final AwsProperties awsProperties;

    public void saveFileMetadata(String id, String fileKey) {
        Map<String, AttributeValue> item = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();

        item.put("id", AttributeValue.builder().s(id).build());
        item.put("uploadTimestamp", AttributeValue.builder().s(Timestamp.valueOf(now).toString()).build());
        item.put("fileKey", AttributeValue.builder().s(fileKey).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(awsProperties.getDynamoDb().getTableName())
                .item(item)
                .build();

        dynamoDbClient.putItem(request);
    }

    public String getFileMetadata(String itemId) {
        String tableName = awsProperties.getDynamoDb().getTableName();

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", AttributeValue.fromS(itemId));

        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();

        GetItemResponse response = dynamoDbClient.getItem(request);

        if (response.hasItem()) {
            Map<String, AttributeValue> item = response.item();
            LOGGER.info("Retrieved item from db: {}", item);

            return item.get("fileKey").s();
        } else {
            throw new RuntimeException(String.format("Item with id: %s was not found", itemId));
        }
    }
}
