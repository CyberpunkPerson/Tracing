package com.github.cyberpunkperson.tracing.handler.domain.source.dc.configuration;

import com.github.cyberpunkperson.tracing.handler.configuration.kafka.properties.KafkaProperties;
import com.github.cyberpunkperson.tracing.handler.configuration.kafka.properties.KafkaProperties.KafkaConsumer;
import com.github.cyberpunkperson.tracing.handler.configuration.kafka.properties.KafkaTopicInformation;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

import static java.util.UUID.randomUUID;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

@EnableKafka
@Configuration
class DcKafkaConfiguration {

    @Bean
    @ConfigurationProperties("kafka.sources.dc")
    KafkaProperties dcKafkaProperties() {
        return new KafkaProperties();
    }

    @Bean
    ConsumerFactory<String, byte[]> dcConsumerFactory(KafkaProperties dcKafkaProperties) {
        KafkaConsumer consumer = dcKafkaProperties.getConsumer();
        Map<String, Object> configuration = new HashMap<>(consumer.getProperties());
        configuration.put(BOOTSTRAP_SERVERS_CONFIG, dcKafkaProperties.getUrl());
        configuration.put(GROUP_ID_CONFIG, "%s.%s".formatted(consumer.getGroupId(), randomUUID()));
        configuration.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configuration.put(VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(configuration);
    }

    @Bean
    @ConfigurationProperties("kafka.topics.dc")
    KafkaTopicInformation dcKafkaTopicInformation() {
        return new KafkaTopicInformation();
    }

    @Bean
    KafkaMessageListenerContainer<String, byte[]> dcContainer(KafkaTopicInformation dcKafkaTopicInformation,
                                                              ConsumerFactory<String, byte[]> dcConsumerFactory) {
        ContainerProperties properties = new ContainerProperties(dcKafkaTopicInformation.getName());
        return new KafkaMessageListenerContainer<>(dcConsumerFactory, properties);
    }
}
