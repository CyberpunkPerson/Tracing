package com.github.cyberpunkperson.tracing.handler.domain.target.news.configuration;

import com.github.cyberpunkperson.tracing.handler.configuration.kafka.properties.KafkaTopicInformation;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.integration.kafka.dsl.KafkaProducerMessageHandlerSpec;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.messaging.MessageChannel;
import src.main.java.com.github.cyberpunkperson.tracing.target.kafka.event.News.NewsEvent;

import static org.springframework.integration.dsl.IntegrationFlows.from;

@EnableKafka
@Configuration
class NewsKafkaConfiguration {

    private static final String HERO_ID = "HERO_ID";


    @Bean
    @ConfigurationProperties("kafka.topics.news-target")
    KafkaTopicInformation newsKafkaTopicInformation() {
        return new KafkaTopicInformation();
    }

    @Bean
    public MessageChannel outboundNewsChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    KafkaProducerMessageHandlerSpec<byte[], byte[], ?> newsOutboundChannelAdapter(ProducerFactory<byte[], byte[]> kafkaDefaultProducerFactory,
                                                                                  KafkaTopicInformation newsKafkaTopicInformation) {
        return Kafka
                .outboundChannelAdapter(kafkaDefaultProducerFactory)
                .topic(newsKafkaTopicInformation.getName())
                .messageKey(message -> message.getHeaders().get(HERO_ID));
    }

    @Bean
    IntegrationFlow newsFlow(KafkaProducerMessageHandlerSpec<byte[], byte[], ?> newsOutboundChannelAdapter,
                             GenericTransformer<NewsEvent, byte[]> newsToByteArrayTransformer) {
        return from(outboundNewsChannel())
                .enrichHeaders(enricher ->
                        enricher.<NewsEvent>headerFunction(HERO_ID, message ->
                                message.getPayload().getHeroId().toByteArray()))
                .transform(newsToByteArrayTransformer)
                .handle(newsOutboundChannelAdapter)
                .get();
    }
}
