package com.group12.social_media_app.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FriendRequestTest {

    private FriendRequest friendRequest;
    private User user;
    private User friend;

    @BeforeEach
    void setUp() {
        friendRequest = new FriendRequest();
        user = new User();
        user.setId(1L);
        friend = new User();
        friend.setId(2L);
    }

    @Test
    void testGetAndSetId() {
        Long id = 1L;
        friendRequest.setId(id);
        assertEquals(id, friendRequest.getId());
    }

    @Test
    void testGetAndSetUser() {
        friendRequest.setUser(user);
        assertEquals(user, friendRequest.getUser());
    }

    @Test
    void testGetAndSetFriend() {
        friendRequest.setFriend(friend);
        assertEquals(friend, friendRequest.getFriend());
    }

    @Test
    void testIsAndSetAccepted() {
        friendRequest.setAccepted(true);
        assertTrue(friendRequest.isAccepted());

        friendRequest.setAccepted(false);
        assertFalse(friendRequest.isAccepted());
    }
}
