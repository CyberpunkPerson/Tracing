package com.github.cyberpunkperson.tracing.handler.domain.source.dc.configuration;

import com.github.cyberpunkperson.tracing.handler.helper.integration.converter.ProtoEventMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.messaging.MessageChannel;
import src.main.java.com.github.cyberpunkperson.tracing.dc.kafka.event.DC.Event;

import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@EnableIntegration
class DcConfiguration {

    @Bean
    KafkaMessageDrivenChannelAdapter<String, byte[]> inboundDcChannelAdapter(KafkaMessageListenerContainer<String, byte[]> dcContainer,
                                                                             MessageChannel integrationErrorChannel) {
        var inboundAdapter = new KafkaMessageDrivenChannelAdapter<>(dcContainer);
        inboundAdapter.setPayloadType(byte.class);
        inboundAdapter.setBindSourceRecord(true);
        inboundAdapter.setMessageConverter(new ProtoEventMessageConverter<>(Event.newBuilder().build().getParserForType()));
        inboundAdapter.setErrorChannel(integrationErrorChannel);
        return inboundAdapter;
    }

    @Bean
    MessageChannel inboundDcChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    IntegrationFlow inboundDcFlow(KafkaMessageDrivenChannelAdapter<String, byte[]> inboundDcChannelAdapter,
                                  MessageChannel inboundDcChannel) {
        return from(inboundDcChannelAdapter)
                .channel(inboundDcChannel)
                .get();
    }
}
