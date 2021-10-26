package com.github.cyberpunkperson.tracing.domain.source.dc.news;

import org.springframework.integration.core.GenericSelector;
import org.springframework.stereotype.Component;
import src.main.java.com.github.cyberpunkperson.tracing.dc.kafka.event.DC.Event;

import static java.util.Objects.nonNull;

@Component
class DcNewsFilter implements GenericSelector<Event> {

    @Override
    public boolean accept(Event event) {
        return nonNull(event) &&
                event.hasNews() &&
                !event.getNews().getId().isEmpty() &&
                !event.getNews().getHeroName().isBlank();
    }
}
