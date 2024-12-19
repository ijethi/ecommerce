package com.group12.social_media_app.dto;

import com.group12.social_media_app.dto.SignUpRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SignUpRequestTest {

    @Test
    void testGettersAndSetters() {
        SignUpRequest request = new SignUpRequest();
        request.setEmail("kr612042@dal.ca");
        request.setPassword("Mamlook0_");
        request.setSecurityQuestion("What school do you go to?");
        request.setSecurityAnswer("Dalhousie");
        request.setInterests("Cars");

        assertEquals("kr612042@dal.ca", request.getEmail());
        assertEquals("Mamlook0_", request.getPassword());
        assertEquals("What school do you go to?", request.getSecurityQuestion());
        assertEquals("Dalhousie", request.getSecurityAnswer());
        assertEquals("Cars", request.getInterests());
    }
}
