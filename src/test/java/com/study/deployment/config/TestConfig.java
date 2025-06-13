package com.study.deployment.config;

import com.study.deployment.service.DescribeEnvProcessor;
import com.study.deployment.service.impl.DescribeTestEnvProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public DescribeEnvProcessor describeTestEnvProcessor() {
        return new DescribeTestEnvProcessor();
    }

}
