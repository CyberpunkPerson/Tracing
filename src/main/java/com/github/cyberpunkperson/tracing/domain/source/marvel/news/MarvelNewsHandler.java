package com.github.cyberpunkperson.tracing.domain.source.marvel.news;

import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import src.main.java.com.github.cyberpunkperson.tracing.marvel.kafka.event.Marvel.Event;
import src.main.java.com.github.cyberpunkperson.tracing.target.kafka.event.News.NewsEvent;

@Component
class MarvelNewsHandler implements GenericHandler<Event> {

    @Override
    public NewsEvent handle(Event marvelEvent, MessageHeaders headers) {
        return null;
    }
}
