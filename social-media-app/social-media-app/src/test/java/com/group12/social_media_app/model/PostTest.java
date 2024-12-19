package com.group12.social_media_app.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PostTest {

    private Post post;
    private User user;

    @BeforeEach
    void setUp() {
        post = new Post();
        user = new User();
        user.setId(1L);
    }

    @Test
    void testGetAndSetId() {
        Long id = 1L;
        post.setId(id);
        assertEquals(id, post.getId());
    }

    @Test
    void testGetAndSetContent() {
        String content = "This is content";
        post.setContent(content);
        assertEquals(content, post.getContent());
    }

    @Test
    void testGetAndSetEmail() {
        String email = "kr612042@dal.ca";
        post.setEmail(email);
        assertEquals(email, post.getEmail());
    }

    @Test
    void testGetAndSetUser() {
        post.setUser(user);
        assertEquals(user, post.getUser());
    }
}
