package com.group12.social_media_app.controller;

import com.group12.social_media_app.config.TestSecurityConfig;
import com.group12.social_media_app.model.User;
import com.group12.social_media_app.service.FriendRecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FriendRecommendationController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class FriendRecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FriendRecommendationService friendRecommendationService;

    @BeforeEach
    void setUp() {
        User user2 = new User("test2@dal.ca", "Password2!", "First pet's name?", "dolu", "Interest2");
        User user3 = new User("test3@dal.ca", "Password3!", "First pet's name?", "dolu1", "Interest3");

        Mockito.when(friendRecommendationService.getRecommendedFriends("test1@dal.ca"))
                .thenReturn(Arrays.asList(user2, user3));
    }
    /**
    @Test
    void testGetRecommendedFriends() throws Exception {
        mockMvc.perform(get("/api/recommendations/friends")
                        .param("email", "test1@dal.ca"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("test2@dal.ca"))
                .andExpect(jsonPath("$[1].email").value("test3@dal.ca"));
    }
    **/
}
