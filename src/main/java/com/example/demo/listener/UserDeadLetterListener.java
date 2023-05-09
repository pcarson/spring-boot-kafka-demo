package com.example.demo.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Consume messages from the user topic
 */
@Component
@Slf4j
public class UserDeadLetterListener {


    // https://www.baeldung.com/spring-kafka

    @KafkaListener(topics = "genericDLT", groupId = "user")
    /**
     * TODO how to get topics and groupId from properties ....
     */
    public void listenForDeadLetterUserRequests(
            @Payload String message,
            @Header(KafkaHeaders.DLT_EXCEPTION_MESSAGE) String exceptionMessage) {
        log.info("Received Dead Letter Message {} with exception {}", message, exceptionMessage);
    }
}
