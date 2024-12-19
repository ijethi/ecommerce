package com.group12.social_media_app.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("kr612042@dal.ca", "Mamlook0_", "What school do you go to?", "Dalhousie", "Cars");
        user.setId(1L);
        user.setStatus("Online");
        user.setRole("User");
        user.setTag("Student");
    }

    @Test
    void testGetAndSetId() {
        Long id = 2L;
        user.setId(id);
        assertEquals(id, user.getId());
    }

    @Test
    void testGetAndSetEmail() {
        String email = "kr612042@dal.ca";
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    void testGetAndSetPassword() {
        String password = "Mamlook0_";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    void testGetAndSetSecurityQuestion() {
        String securityQuestion = "What school do you go to?";
        user.setSecurityQuestion(securityQuestion);
        assertEquals(securityQuestion, user.getSecurityQuestion());
    }

    @Test
    void testGetAndSetSecurityAnswer() {
        String securityAnswer = "Dalhousie";
        user.setSecurityAnswer(securityAnswer);
        assertEquals(securityAnswer, user.getSecurityAnswer());
    }

    @Test
    void testGetAndSetStatus() {
        String status = "Away";
        user.setStatus(status);
        assertEquals(status, user.getStatus());
    }

    @Test
    void testGetAndSetInterests() {
        String interests = "Music";
        user.setInterests(interests);
        assertEquals(interests, user.getInterests());
    }

    @Test
    void testGetAndSetPendingFriendRequests() {
        List<Long> pendingFriendRequests = new ArrayList<>();
        pendingFriendRequests.add(3L);
        pendingFriendRequests.add(4L);
        user.setPendingFriendRequests(pendingFriendRequests);
        assertEquals(pendingFriendRequests, user.getPendingFriendRequests());
    }

    @Test
    void testGetAndSetFriends() {
        List<Long> friends = new ArrayList<>();
        friends.add(5L);
        friends.add(6L);
        user.setFriends(friends);
        assertEquals(friends, user.getFriends());
    }

    @Test
    void testGetAndSetRole() {
        String role = "Admin";
        user.setRole(role);
        assertEquals(role, user.getRole());
    }

    @Test
    void testGetAndSetTag() {
        String tag = "Software Engineer";
        user.setTag(tag);
        assertEquals(tag, user.getTag());
    }
}
