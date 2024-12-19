package com.group12.social_media_app.dto;

import com.group12.social_media_app.dto.LoginRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void testGettersAndSetters() {
        LoginRequest request = new LoginRequest();
        request.setEmail("kr612042@dal.ca");
        request.setPassword("Mamlook0_");

        assertEquals("kr612042@dal.ca", request.getEmail());
        assertEquals("Mamlook0_", request.getPassword());
    }
}
