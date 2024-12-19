package com.group12.social_media_app.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.group12.social_media_app.model.Post;
import com.group12.social_media_app.model.User;
import com.group12.social_media_app.repository.PostRepository;
import com.group12.social_media_app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPosts() {
        Post post1 = new Post();
        post1.setUser(new User());
        Post post2 = new Post();
        post2.setUser(new User());
        List<Post> posts = Arrays.asList(post1, post2);

        when(postRepository.findAll()).thenReturn(posts);

        List<Post> result = postService.getAllPosts();

        assertEquals(2, result.size());
        assertTrue(result.contains(post1));
        assertTrue(result.contains(post2));
    }

    @Test
    public void testSavePost() {
        Post post = new Post();
        post.setUser(new User());
        when(postRepository.save(post)).thenReturn(post);

        Post result = postService.savePost(post);

        assertEquals(post, result);
        verify(postRepository, times(1)).save(post);
    }

    @Test
    public void testGetPostsForUser() {
        String email = "test@dal.ca";
        User user = new User();
        user.setEmail(email);
        user.setFriends(Arrays.asList(1L, 2L));

        User friend1 = new User();
        friend1.setId(1L);
        User friend2 = new User();
        friend2.setId(2L);

        Post post1 = new Post();
        post1.setUser(user);
        Post post2 = new Post();
        post2.setUser(friend1);
        Post post3 = new Post();
        post3.setUser(friend2);
        Post post4 = new Post();
        post4.setUser(new User());

        List<Post> allPosts = Arrays.asList(post1, post2, post3, post4);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(friend1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(friend2));
        when(postRepository.findAll()).thenReturn(allPosts);

        List<Post> result = postService.getPostsForUser(email);

        assertEquals(3, result.size());
        assertTrue(result.contains(post1));
        assertTrue(result.contains(post2));
        assertTrue(result.contains(post3));
        assertFalse(result.contains(post4));
    }

    @Test
    public void testGetPostsForUserWithNoFriends() {
        String email = "test@dal.ca";
        User user = new User();
        user.setEmail(email);
        user.setFriends(Collections.emptyList());

        Post post1 = new Post();
        post1.setUser(user);
        Post post2 = new Post();
        post2.setUser(new User());

        List<Post> allPosts = Arrays.asList(post1, post2);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(postRepository.findAll()).thenReturn(allPosts);

        List<Post> result = postService.getPostsForUser(email);

        assertEquals(1, result.size());
        assertTrue(result.contains(post1));
        assertFalse(result.contains(post2));
    }

    @Test
    public void testGetPostsForUserWithMissingFriends() {
        String email = "test@dal.ca";
        User user = new User();
        user.setEmail(email);
        user.setFriends(Arrays.asList(1L, 2L));

        User friend1 = new User();
        friend1.setId(1L);

        Post post1 = new Post();
        post1.setUser(user);
        Post post2 = new Post();
        post2.setUser(friend1);
        Post post3 = new Post();
        post3.setUser(new User());

        List<Post> allPosts = Arrays.asList(post1, post2, post3);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(friend1));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        when(postRepository.findAll()).thenReturn(allPosts);

        List<Post> result = postService.getPostsForUser(email);

        assertEquals(2, result.size());
        assertTrue(result.contains(post1));
        assertTrue(result.contains(post2));
        assertFalse(result.contains(post3));
    }


}
