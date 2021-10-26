package com.github.cyberpunkperson.tracing.helper.integration.converter;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.converter.MessagingMessageConverter;

import java.lang.reflect.Type;

@RequiredArgsConstructor
public class ProtoEventMessageConverter<EventType extends MessageLite> extends MessagingMessageConverter {

    private final Parser<EventType> parser;


    @Override
    protected EventType extractAndConvertValue(ConsumerRecord<?, ?> record, Type type) {
        try {
            return parser.parseFrom((byte[]) record.value());
        } catch (InvalidProtocolBufferException e) {
            throw new IllegalArgumentException("Failed to deserialize event", e);
        }
    }
}
