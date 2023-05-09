package com.example.demo.listener;

import com.example.demo.TestDataPreparationUtils;
import com.example.demo.exception.UserExistsException;
import com.example.demo.exception.UserServiceException;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDeadLetterListenerTest implements TestDataPreparationUtils {

    @InjectMocks
    UserDeadLetterListener listener;

    @Test
    void consumeNewUserRequestSuccessfullyAddedtoDatabase() throws UserExistsException, UserServiceException, JsonProcessingException {

        var userDto = getDummyUserDTO();
        var jsonised = new ObjectMapper().writeValueAsString(userDto);
        listener.listenForDeadLetterUserRequests(jsonised, "didn't work");
    }

}