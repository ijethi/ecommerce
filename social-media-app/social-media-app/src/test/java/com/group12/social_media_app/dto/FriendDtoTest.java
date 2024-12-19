package com.group12.social_media_app.dto;

import com.group12.social_media_app.dto.FriendDto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FriendDtoTest {

    @Test
    void testConstructorAndGetters() {
        FriendDto dto = new FriendDto("kr612042@dal.ca", "accepted");

        assertEquals("kr612042@dal.ca", dto.getEmail());
        assertEquals("accepted", dto.getStatus());
    }

    @Test
    void testSetters() {
        FriendDto dto = new FriendDto("kr612042@dal.ca", "accepted");
        dto.setEmail("kr612042@dal.ca");
        dto.setStatus("pending");

        assertEquals("kr612042@dal.ca", dto.getEmail());
        assertEquals("pending", dto.getStatus());
    }
}
