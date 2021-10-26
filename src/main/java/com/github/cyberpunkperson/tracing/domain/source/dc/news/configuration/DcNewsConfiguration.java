package com.github.cyberpunkperson.tracing.domain.source.dc.news.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.GenericSelector;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageChannel;
import src.main.java.com.github.cyberpunkperson.tracing.dc.kafka.event.DC.Event;

import static com.github.cyberpunkperson.tracing.helper.logger.MdcKey.OPERATION_NAME;
import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@EnableIntegration
class DcNewsConfiguration {

    private static final String DC_NEWS_HANDLE = "DC_NEWS_HANDLE";


    @Bean
    IntegrationFlow dcNewsFlow(MessageChannel inboundDcChannel,
                               GenericSelector<Event> dcNewsFilter,
                               GenericHandler<Event> dcNewsHandler,
                               MessageChannel outboundNewsChannel) {
        return from(inboundDcChannel)
                .filter(dcNewsFilter)
                .enrichHeaders(enricher -> enricher.header(OPERATION_NAME, DC_NEWS_HANDLE))
                .handle(dcNewsHandler)
                .channel(outboundNewsChannel)
                .get();
    }
}
