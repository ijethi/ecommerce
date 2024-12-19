package com.group12.social_media_app.dto;

import com.group12.social_media_app.dto.ChangeRoleRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChangeRoleRequestTest {

    @Test
    void testConstructorAndGetters() {
        ChangeRoleRequest request = new ChangeRoleRequest("kr612042@dal.ca", "admin");

        assertEquals("kr612042@dal.ca", request.getEmail());
        assertEquals("admin", request.getNewRole());
    }

    @Test
    void testSetters() {
        ChangeRoleRequest request = new ChangeRoleRequest("user@dal.ca", "admin");
        request.setEmail("kr612042@dal.ca");
        request.setNewRole("user");

        assertEquals("kr612042@dal.ca", request.getEmail());
        assertEquals("user", request.getNewRole());
    }
}
