package com.github.cyberpunkperson.tracing.handler.domain.source.marvel.news;

import org.springframework.integration.core.GenericSelector;
import org.springframework.stereotype.Component;
import src.main.java.com.github.cyberpunkperson.tracing.marvel.kafka.event.Marvel.Event;

import static java.util.Objects.nonNull;

@Component
class MarvelNewsFilter implements GenericSelector<Event> {

    @Override
    public boolean accept(Event event) {
        return nonNull(event) &&
                event.hasNews() &&
                !event.getNews().getId().isEmpty() &&
                !event.getNews().getHeroName().isBlank();
    }
}
