package com.example.demo.listener;

import com.example.demo.dto.UserDTO;
import com.example.demo.exception.UserExistsException;
import com.example.demo.exception.UserServiceException;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Consume messages from the user topic
 */
@Component
@Slf4j
public class UserListener {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    public UserListener(UserService userService,
                        ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    // https://www.baeldung.com/spring-kafka

    @KafkaListener(topics = "user", groupId = "user")
    /**
     * TODO how to get topics and groupId from properties ....
     */
    public void listenForNewUserRequests(@Payload String message) {
        log.info("Received Message {}", message);
        // still here? Add to DB
        try {
            var userDto = objectMapper.readValue(message, UserDTO.class);
            userService.createUser(userDto);
        } catch (UserExistsException | UserServiceException | JsonProcessingException exes) {
            log.error("Couldn't add a user from Kafka", exes);
            // trigger a dead letter event ..
            throw new RuntimeException(exes);
        }
    }
}
