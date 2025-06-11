package com.study.deployment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("aws")
public class AwsProperties {

    private String region;
    private final S3Properties s3 = new S3Properties();
    private final DynamoDbProperties dynamoDb = new DynamoDbProperties();
    private final SqsProperties sqs = new SqsProperties();

    @Data
    public static class SqsProperties {
        private String queueUrl;
    }
    @Data
    public static class DynamoDbProperties {

        private String tableName;
    }
    @Data
    public static class S3Properties {

        private String bucketName;
    }
}


