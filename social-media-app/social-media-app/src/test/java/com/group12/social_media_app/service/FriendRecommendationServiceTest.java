package com.group12.social_media_app.service;

import com.group12.social_media_app.model.User;
import com.group12.social_media_app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class FriendRecommendationServiceTest {

    private UserRepository userRepository;
    private FriendRecommendationService friendRecommendationService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        friendRecommendationService = new FriendRecommendationService(userRepository);
    }
    /**
    @Test
    void testGetRecommendedFriends() {
        User user1 = new User("test1@dal.ca", "Password1!", "First pet's name?", "dolu", "Interest1");
        User user2 = new User("test2@dal.ca", "Password2!", "First pet's name?", "dolu2", "Interest2");
        User user3 = new User("test3@dal.ca", "Password3!", "First pet's name?", "dolu3", "Interest3");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2, user3));

        List<User> recommendedFriends = friendRecommendationService.getRecommendedFriends("test1@dal.ca");

        assertEquals(2, recommendedFriends.size());
        assertTrue(recommendedFriends.contains(user2));
        assertTrue(recommendedFriends.contains(user3));
    }
    **/
}
