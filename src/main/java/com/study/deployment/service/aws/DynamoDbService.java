package com.study.deployment.service.aws;

import com.study.deployment.config.AwsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DynamoDbService {

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
}
