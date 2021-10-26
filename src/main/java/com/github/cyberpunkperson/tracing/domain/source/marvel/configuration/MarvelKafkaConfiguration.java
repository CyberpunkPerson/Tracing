package com.github.cyberpunkperson.tracing.domain.source.marvel.configuration;

import com.github.cyberpunkperson.tracing.configuration.kafka.properties.KafkaProperties;
import com.github.cyberpunkperson.tracing.configuration.kafka.properties.KafkaProperties.KafkaConsumer;
import com.github.cyberpunkperson.tracing.configuration.kafka.properties.KafkaTopicInformation;
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

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

@EnableKafka
@Configuration
class MarvelKafkaConfiguration {

    @Bean
    @ConfigurationProperties("kafka.sources.marvel")
    KafkaProperties marvelKafkaProperties() {
        return new KafkaProperties();
    }

    @Bean
    ConsumerFactory<String, byte[]> marvelConsumerFactory(KafkaProperties marvelKafkaProperties) {
        KafkaConsumer consumer = marvelKafkaProperties.getConsumer();
        Map<String, Object> configuration = new HashMap<>(consumer.getProperties());
        configuration.put(BOOTSTRAP_SERVERS_CONFIG, marvelKafkaProperties.getUrl());
        configuration.put(GROUP_ID_CONFIG, consumer.getGroupId());
        configuration.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configuration.put(VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(configuration);
    }

    @Bean
    @ConfigurationProperties("kafka.topics.marvel")
    KafkaTopicInformation marvelKafkaTopicInformation() {
        return new KafkaTopicInformation();
    }

    @Bean
    KafkaMessageListenerContainer<String, byte[]> marvelContainer(KafkaTopicInformation marvelKafkaTopicInformation,
                                                                  ConsumerFactory<String, byte[]> marvelConsumerFactory) {
        ContainerProperties properties = new ContainerProperties(marvelKafkaTopicInformation.getName());
        return new KafkaMessageListenerContainer<>(marvelConsumerFactory, properties);
    }
}
