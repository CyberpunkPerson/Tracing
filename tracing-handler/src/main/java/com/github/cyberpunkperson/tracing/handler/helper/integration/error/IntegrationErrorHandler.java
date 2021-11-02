package com.github.cyberpunkperson.tracing.handler.helper.integration.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.converter.ConversionException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import static com.github.cyberpunkperson.tracing.handler.helper.logger.MdcKey.OPERATION_NAME;
import static java.util.Optional.ofNullable;

@Slf4j
@Component
class IntegrationErrorHandler implements MessageHandler {

    @Override
    public void handleMessage(Message<?> failedMessage) {

        if (failedMessage.getPayload() instanceof MessagingException exception) {

            ofNullable(exception.getFailedMessage())
                    .ifPresentOrElse(message ->
                                    log.error("Integration error for {} operation with cause:",
                                            message.getHeaders().get(OPERATION_NAME),
                                            exception.getCause()),
                            () -> log.error("Integration error with cause:", exception.getCause()));

        } else if (failedMessage.getPayload() instanceof ConversionException exception) {
            ofNullable(exception.getRecord())
                    .ifPresentOrElse(record ->
                                    log.error("Failed to convert record {} with cause:",
                                            record,
                                            exception.getCause()),

                            () -> log.error("Failed to convert records: {} with exception: {} ",
                                    exception.getRecords(),
                                    exception.getCause()));

            ;
        } else
            log.error("Integration error with payload: {}", failedMessage.getPayload());
    }
}
