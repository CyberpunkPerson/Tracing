package com.github.cyberpunkperson.tracing.spammer;

import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import src.main.java.com.github.cyberpunkperson.tracing.dc.kafka.event.DC;
import src.main.java.com.github.cyberpunkperson.tracing.marvel.kafka.event.Marvel;

import java.util.stream.Stream;

import static com.github.cyberpunkperson.tracing.spammer.utils.ProtobufUtils.byteStringToUUIDString;
import static com.github.cyberpunkperson.tracing.spammer.utils.ProtobufUtils.uuidToByteString;
import static java.util.UUID.randomUUID;
import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static src.main.java.com.github.cyberpunkperson.tracing.dc.kafka.event.DC.Event.Action.FEAT;
import static src.main.java.com.github.cyberpunkperson.tracing.marvel.kafka.event.Marvel.Event.Action.VICTORY;

@Component
@RequiredArgsConstructor
class Spammer implements ApplicationListener<ApplicationStartedEvent> {

    @Value("${spammer.records.marvel.amount}")
    private Long marvelRecordsAmount;

    @Value("${kafka.topic.marvel.name}")
    private String marvelOutboundTopic;

    @Value("${kafka.topic.dc.name}")
    private String dcOutboundTopic;

    @Value("${spammer.records.dc.amount}")
    private Long dcRecordsAmount;

    private final KafkaSender kafkaSender;


    @Override
    @SneakyThrows
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {

        var marvelDispatchFuture = supplyAsync(() -> generateMarvelEvents(marvelRecordsAmount))
                .thenAccept(events -> events
                        .forEach(event -> send(marvelOutboundTopic, event.getNews().getHeroId(), event.toByteArray())));

        var dcDispatchFuture = supplyAsync(() -> generateDcEvents(dcRecordsAmount))
                .thenAccept(events -> events
                        .forEach(event -> send(dcOutboundTopic, event.getNews().getHeroId(), event.toByteArray())));

        allOf(marvelDispatchFuture, dcDispatchFuture)
                .thenRun(() -> System.exit(0))
                .get();
    }

    private void send(String topic, ByteString key, byte[] payload) {
        kafkaSender.send(topic, byteStringToUUIDString(key), payload);
    }

    private Stream<DC.Event> generateDcEvents(Long recordsAmount) {
        Stream.Builder<DC.Event> builder = Stream.builder();
        for (long index = 0; index < recordsAmount; index++) {
            builder.add(DC.Event.newBuilder()
                    .setId(uuidToByteString(randomUUID()))
                    .setNews(DC.Event.News.newBuilder()
                            .setId(uuidToByteString(randomUUID()))
                            .setHeroId(uuidToByteString(randomUUID()))
                            .setAction(FEAT)
                            .setHeroName("Does not really matter")
                            .setDescription("Does not really matter either")
                            .build())
                    .build());
        }
        return builder.build();
    }

    private Stream<Marvel.Event> generateMarvelEvents(Long recordsAmount) {
        Stream.Builder<Marvel.Event> builder = Stream.builder();
        for (long index = 0; index < recordsAmount; index++) {
            builder.add(Marvel.Event.newBuilder()
                    .setId(uuidToByteString(randomUUID()))
                    .setNews(Marvel.Event.News.newBuilder()
                            .setId(uuidToByteString(randomUUID()))
                            .setHeroId(uuidToByteString(randomUUID()))
                            .setAction(VICTORY)
                            .setHeroName("Does not really matter")
                            .setDescription("Does not really matter either")
                            .build())
                    .build());
        }
        return builder.build();
    }
}
