package com.github.cyberpunkperson.tracing.helper.integration.error;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;

import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@EnableIntegration
class IntegrationErrorChanelConfiguration {

    @Bean
    public MessageChannel integrationErrorChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    IntegrationFlow integrationErrorFlow(IntegrationErrorHandler integrationErrorHandler) {
        return from(integrationErrorChannel())
                .handle(integrationErrorHandler)
                .get();
    }
}
