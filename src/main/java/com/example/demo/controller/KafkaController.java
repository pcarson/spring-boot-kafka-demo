package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Tag(name = "KafkaController", description = "Endpoint for publishing items to Kafka")
@Controller
public class KafkaController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.user.topic}")
    private String topicName;

    public KafkaController(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Operation(summary = "Create kafka user entry", description = "Publishes new user details to kafka.")
    @PostMapping("/kafka/user")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {

        try {
            // kafkaTemplate.send(topicName, userDTO);
            sendMessageWIthCompletionLogging(userDTO);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private void sendMessageWIthCompletionLogging(UserDTO userDTO) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName, userDTO);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Sent message=[{}] with offset=[{}]", userDTO, result.getRecordMetadata().offset());
            } else {
                log.error("Unable to send message=[" + userDTO + "] due to : " + ex.getMessage());
            }
        });
    }

}
