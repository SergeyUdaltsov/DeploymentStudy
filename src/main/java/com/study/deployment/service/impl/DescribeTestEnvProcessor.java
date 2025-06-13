package com.study.deployment.service.impl;

import com.study.deployment.service.DescribeEnvProcessor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component()
@Profile("test")
public class DescribeTestEnvProcessor implements DescribeEnvProcessor {

    @Override
    public String describeEnv() {
        return "This is the TEST env description";
    }
}
