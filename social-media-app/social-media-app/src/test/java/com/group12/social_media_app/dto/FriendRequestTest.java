package com.group12.social_media_app.dto;

import com.group12.social_media_app.dto.FriendRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FriendRequestTest {

    @Test
    void testConstructorAndGetters() {
        FriendRequest request = new FriendRequest(1L, "kr612042@dal.ca", "ma612042@dal.ca");

        assertEquals(1L, request.getId());
        assertEquals("kr612042@dal.ca", request.getUserEmail());
        assertEquals("ma612042@dal.ca", request.getFriendEmail());
    }

    @Test
    void testDefaultConstructorAndSetters() {
        FriendRequest request = new FriendRequest();
        request.setId(2L);
        request.setUserEmail("ka612042@dal.ca");
        request.setFriendEmail("mm612042@dal.ca");

        assertEquals(2L, request.getId());
        assertEquals("ka612042@dal.ca", request.getUserEmail());
        assertEquals("mm612042@dal.ca", request.getFriendEmail());
    }
}
