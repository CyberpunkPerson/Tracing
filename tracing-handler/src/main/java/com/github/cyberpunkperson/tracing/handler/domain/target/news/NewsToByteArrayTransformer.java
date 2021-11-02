package com.github.cyberpunkperson.tracing.handler.domain.target.news;

import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.stereotype.Component;
import src.main.java.com.github.cyberpunkperson.tracing.target.kafka.event.News.NewsEvent;

@Component
class NewsToByteArrayTransformer implements GenericTransformer<NewsEvent, byte[]> {


    @Override
    public byte[] transform(NewsEvent newsEvent) {
        return newsEvent.toByteArray();
    }
}
