package com.github.cyberpunkperson.tracing.handler.domain.source.dc.news;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import src.main.java.com.github.cyberpunkperson.tracing.dc.kafka.event.DC.Event;
import src.main.java.com.github.cyberpunkperson.tracing.dc.kafka.event.DC.Event.News;
import src.main.java.com.github.cyberpunkperson.tracing.target.kafka.event.News.NewsEvent;
import src.main.java.com.github.cyberpunkperson.tracing.target.kafka.event.News.NewsEvent.Action;

import static com.github.cyberpunkperson.tracing.handler.helper.utils.ProtobufUtils.byteStringToUUIDString;

@Slf4j
@Component
class DcNewsHandler implements GenericHandler<Event> {

    @Override
    public NewsEvent handle(Event dcEvent, MessageHeaders headers) {
        News news = dcEvent.getNews();
        Action action = switch (news.getAction()) {
            case FEAT -> Action.FEAT;
            case FUCK_UP -> Action.FUCK_UP;
            default -> throw new IllegalArgumentException("Unsupported action received");
        };
        log.info("Handling DC event with id: '%s'".formatted(dcEvent.getId()));
        return NewsEvent.newBuilder()
                .setId(dcEvent.getId())
                .setHeroId(news.getHeroId())
                .setHeroName(news.getHeroName())
                .setDescription(news.getDescription())
                .setAction(action)
                .setEntityIdempotencyKey(byteStringToUUIDString(news.getHeroId()))
                .build();
    }
}
