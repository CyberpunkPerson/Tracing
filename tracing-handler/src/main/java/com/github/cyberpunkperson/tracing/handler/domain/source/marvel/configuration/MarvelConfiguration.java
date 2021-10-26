package com.github.cyberpunkperson.tracing.handler.domain.source.marvel.configuration;

import com.github.cyberpunkperson.tracing.handler.helper.integration.converter.ProtoEventMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.messaging.MessageChannel;
import src.main.java.com.github.cyberpunkperson.tracing.marvel.kafka.event.Marvel.Event;

import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@EnableIntegration
class MarvelConfiguration {

    @Bean
    KafkaMessageDrivenChannelAdapter<String, byte[]> inboundMarvelChannelAdapter(KafkaMessageListenerContainer<String, byte[]> marvelContainer,
                                                                                 MessageChannel integrationErrorChannel) {
        var inboundAdapter = new KafkaMessageDrivenChannelAdapter<>(marvelContainer);
        inboundAdapter.setPayloadType(byte.class);
        inboundAdapter.setBindSourceRecord(true);
        inboundAdapter.setMessageConverter(new ProtoEventMessageConverter<>(Event.newBuilder().build().getParserForType()));
        inboundAdapter.setErrorChannel(integrationErrorChannel);
        return inboundAdapter;
    }

    @Bean
    MessageChannel inboundMarvelChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    IntegrationFlow inboundMarvelFlow(KafkaMessageDrivenChannelAdapter<String, byte[]> inboundMarvelChannelAdapter,
                                      MessageChannel inboundMarvelChannel) {
        return from(inboundMarvelChannelAdapter)
                .channel(inboundMarvelChannel)
                .get();
    }
}
