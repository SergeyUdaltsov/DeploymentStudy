package com.study.deployment.service.impl;

import com.study.deployment.service.DescribeEnvProcessor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component()
@Profile("dev")
public class DescribeDevEnvProcessor implements DescribeEnvProcessor {

    @Override
    public String describeEnv() {
        return "This is the DEV env description";
    }
}
