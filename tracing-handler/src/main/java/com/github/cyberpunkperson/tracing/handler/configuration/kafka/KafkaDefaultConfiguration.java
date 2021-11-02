package com.github.cyberpunkperson.tracing.handler.configuration.kafka;


import com.github.cyberpunkperson.tracing.handler.configuration.kafka.properties.KafkaProperties;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

@EnableKafka
@Configuration
class KafkaDefaultConfiguration {

    @Bean
    ProducerFactory<byte[], byte[]> kafkaDefaultProducerFactory(KafkaProperties kafkaDefaultProperties) {
        Map<String, Object> configuration = new HashMap<>(kafkaDefaultProperties.getProducer());
        configuration.put(BOOTSTRAP_SERVERS_CONFIG, kafkaDefaultProperties.getUrl());
        configuration.put(KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        configuration.put(VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);

        return new DefaultKafkaProducerFactory<>(configuration);
    }

    @Bean
    KafkaTemplate<byte[], byte[]> kafkaDefaultTemplate(ProducerFactory<byte[], byte[]> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    @ConfigurationProperties("kafka.default")
    KafkaProperties kafkaDefaultProperties() {
        return new KafkaProperties();
    }
}

