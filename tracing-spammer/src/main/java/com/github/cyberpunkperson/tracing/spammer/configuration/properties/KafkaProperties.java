package com.github.cyberpunkperson.tracing.spammer.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties("kafka")
public class KafkaProperties {

    private Map<String, String> producer = new HashMap<>();

}
