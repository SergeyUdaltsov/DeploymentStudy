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


    @Data
    public static class S3Properties {

        private String bucketName;
    }
}


