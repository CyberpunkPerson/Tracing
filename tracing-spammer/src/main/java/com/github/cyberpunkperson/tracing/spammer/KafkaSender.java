package com.github.cyberpunkperson.tracing.spammer;

import com.github.cyberpunkperson.tracing.spammer.configuration.properties.KafkaProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

import java.util.Properties;

import static java.util.Optional.ofNullable;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

@Slf4j
@Service
public class KafkaSender {

    private final KafkaProducer<String, byte[]> kafkaProducer;

    public KafkaSender(KafkaProperties producerProperties) {
        this.kafkaProducer = ofNullable(producerProperties)
                .map(properties -> {
                    Properties configuration = new Properties();
                    configuration.putAll(producerProperties.getProducer());
                    configuration.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
                    configuration.put(VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
                    return new KafkaProducer<String, byte[]>(configuration);
                })
                .orElseThrow(() -> new IllegalArgumentException("Undefined properties passed"));
    }

    public void send(String topic, String key, byte[] payload) {
        kafkaProducer.send(new ProducerRecord<>(topic, key, payload));
        flush();
    }

    public void send(String topic, String key, byte[] payload, Headers headers) {
        kafkaProducer.send(new ProducerRecord<>(topic, null, null, key, payload, headers));
        flush();
    }

    private void flush() {
        kafkaProducer.flush();
    }
}
