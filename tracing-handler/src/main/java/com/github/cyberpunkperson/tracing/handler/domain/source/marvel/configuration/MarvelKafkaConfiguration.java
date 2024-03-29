package com.github.cyberpunkperson.tracing.handler.domain.source.marvel.configuration;

import com.github.cyberpunkperson.tracing.handler.configuration.kafka.properties.KafkaProperties;
import com.github.cyberpunkperson.tracing.handler.configuration.kafka.properties.KafkaProperties.KafkaConsumer;
import com.github.cyberpunkperson.tracing.handler.configuration.kafka.properties.KafkaTopicInformation;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

import static java.util.UUID.randomUUID;
import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;

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
        configuration.put(GROUP_ID_CONFIG, "%s.%s".formatted(consumer.getGroupId(), randomUUID()));
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
    ConcurrentKafkaListenerContainerFactory<String, byte[]> marvelContainerFactory(ConsumerFactory<String, byte[]> marvelConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, byte[]> container = new ConcurrentKafkaListenerContainerFactory<>();
        container.setConsumerFactory(marvelConsumerFactory);
        container.setConcurrency(1);
        return container;
    }

    @Bean
    ConcurrentMessageListenerContainer<String, byte[]> marvelContainer(KafkaTopicInformation marvelKafkaTopicInformation,
                                                                          ConcurrentKafkaListenerContainerFactory<String, byte[]> marvelContainerFactory) {
        return marvelContainerFactory.createContainer(marvelKafkaTopicInformation.getName());
    }
}
