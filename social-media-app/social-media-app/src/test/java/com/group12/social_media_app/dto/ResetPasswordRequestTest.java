package com.group12.social_media_app.dto;

import com.group12.social_media_app.dto.ResetPasswordRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResetPasswordRequestTest {

    @Test
    void testGettersAndSetters() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setEmail("kr612042@dal.ca");
        request.setSecurityAnswer("Dalhousie");
        request.setNewPassword("Mamlook1_");

        assertEquals("kr612042@dal.ca", request.getEmail());
        assertEquals("Dalhousie", request.getSecurityAnswer());
        assertEquals("Mamlook1_", request.getNewPassword());
    }
}
