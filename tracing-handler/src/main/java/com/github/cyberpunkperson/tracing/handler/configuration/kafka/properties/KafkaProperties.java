package com.github.cyberpunkperson.tracing.handler.configuration.kafka.properties;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class KafkaProperties {

    private String url;

    private KafkaConsumer consumer;

    private Map<String, String> producer = new HashMap<>();

    @Data
    public static class KafkaConsumer {

        private String groupId;

        private Map<String, String> properties = new HashMap<>();
    }
}
