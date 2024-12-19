package com.group12.social_media_app.controller;

import com.group12.social_media_app.model.User;
import com.group12.social_media_app.service.FriendRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/recommendations")
public class FriendRecommendationController {

    @Autowired
    private FriendRecommendationService friendRecommendationService;

    @GetMapping("/friends")
    public ResponseEntity<List<User>> getRecommendedFriends(@RequestParam String email) {
        List<User> recommendedFriends = friendRecommendationService.getRecommendedFriends(email);
        return ResponseEntity.ok(recommendedFriends);
    }
}
