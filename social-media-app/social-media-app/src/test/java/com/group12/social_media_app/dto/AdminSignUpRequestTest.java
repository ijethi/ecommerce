package com.group12.social_media_app.dto;

import com.group12.social_media_app.dto.AdminSignUpRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdminSignUpRequestTest {

    @Test
    void testGettersAndSetters() {
        AdminSignUpRequest request = new AdminSignUpRequest();
        request.setEmail("kr612042@dal.ca");
        request.setPassword("Mamlook0_");
        request.setSecurityQuestion("What school do you go to?");
        request.setSecurityAnswer("Dalhousie");
        request.setSecretKey("secretKey");
        request.setInterests("Cars");

        assertEquals("kr612042@dal.ca", request.getEmail());
        assertEquals("Mamlook0_", request.getPassword());
        assertEquals("What school do you go to?", request.getSecurityQuestion());
        assertEquals("Dalhousie", request.getSecurityAnswer());
        assertEquals("secretKey", request.getSecretKey());
        assertEquals("Cars", request.getInterests());
    }
}
