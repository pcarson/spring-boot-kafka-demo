package com.example.demo;

import com.example.demo.dto.UserDTO;

import java.util.Date;
import java.util.UUID;

public interface TestDataPreparationUtils {

    default UserDTO getDummyUserDTO() {
        var user = new UserDTO();
        user.setId(UUID.randomUUID().toString());
        user.setEmail("x@y.com");
        user.setPassword("ssshhhh");
        user.setCreated(new Date());
        user.setLastModified(new Date());
        return user;
    }
}
