package com.github.cyberpunkperson.tracing.handler.domain.source.dc.news;

import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import src.main.java.com.github.cyberpunkperson.tracing.dc.kafka.event.DC.Event;
import src.main.java.com.github.cyberpunkperson.tracing.target.kafka.event.News.NewsEvent;

@Component
class DcNewsHandler implements GenericHandler<Event> {

    @Override
    public NewsEvent handle(Event dcEvent, MessageHeaders headers) {
        return null;
    }
}
