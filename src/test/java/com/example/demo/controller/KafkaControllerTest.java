package com.example.demo.controller;

import com.example.demo.TestDataPreparationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KafkaControllerTest implements TestDataPreparationUtils {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private KafkaController controller;

    @Mock
    private CompletableFuture completableFuture;

    @Test
    void testUserCreate() {
        // succeed ...
        when(kafkaTemplate.send(any(), any())).thenReturn(completableFuture);
        ResponseEntity<?> response = controller.createUser(getDummyUserDTO());
        verify(kafkaTemplate).send(any(), any());
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    void testUserCreateUserDisasterStrikes() {
        // succeed ...
        when(kafkaTemplate.send(any(), any())).thenThrow(new NullPointerException(""));
        ResponseEntity<?> response = controller.createUser(getDummyUserDTO());
        verify(kafkaTemplate).send(any(), any());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}