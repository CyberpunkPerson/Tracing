package com.github.cyberpunkperson.tracing.handler.domain.source.marvel.news.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.GenericSelector;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageChannel;
import src.main.java.com.github.cyberpunkperson.tracing.marvel.kafka.event.Marvel.Event;

import static com.github.cyberpunkperson.tracing.handler.helper.logger.MdcKey.OPERATION_NAME;
import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@EnableIntegration
class MarvelNewsConfiguration {

    private static final String MARVEL_NEWS_HANDLE = "MARVEL_NEWS_HANDLE";


    @Bean
    IntegrationFlow marvelNewsFlow(MessageChannel inboundMarvelChannel,
                                   GenericSelector<Event> marvelNewsFilter,
                                   GenericHandler<Event> marvelNewsHandler,
                                   MessageChannel outboundNewsChannel) {
        return from(inboundMarvelChannel)
                .filter(marvelNewsFilter)
                .enrichHeaders(enricher -> enricher.header(OPERATION_NAME, MARVEL_NEWS_HANDLE))
                .handle(marvelNewsHandler)
                .channel(outboundNewsChannel)
                .get();
    }
}
