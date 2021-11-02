package com.github.cyberpunkperson.tracing.handler.domain.source.marvel.news;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import src.main.java.com.github.cyberpunkperson.tracing.marvel.kafka.event.Marvel.Event;
import src.main.java.com.github.cyberpunkperson.tracing.marvel.kafka.event.Marvel.Event.News;
import src.main.java.com.github.cyberpunkperson.tracing.target.kafka.event.News.NewsEvent;
import src.main.java.com.github.cyberpunkperson.tracing.target.kafka.event.News.NewsEvent.Action;

import static com.github.cyberpunkperson.tracing.handler.helper.utils.ProtobufUtils.byteStringToUUIDString;

@Slf4j
@Component
class MarvelNewsHandler implements GenericHandler<Event> {

    @Override
    public NewsEvent handle(Event marvelEvent, MessageHeaders headers) {
        News news = marvelEvent.getNews();
        Action action = switch (news.getAction()) {
            case VICTORY -> Action.FEAT;
            case FUCK_UP -> Action.FUCK_UP;
            default -> throw new IllegalArgumentException("Unsupported action received");
        };
        log.info("Handling Marvel event with id: '%s'".formatted(marvelEvent.getId()));
        return NewsEvent.newBuilder()
                .setId(marvelEvent.getId())
                .setHeroId(news.getHeroId())
                .setHeroName(news.getHeroName())
                .setDescription(news.getDescription())
                .setAction(action)
                .setEntityIdempotencyKey(byteStringToUUIDString(news.getHeroId()))
                .build();
    }
}
