package com.group12.social_media_app.service;

import com.group12.social_media_app.exception.UserNotFoundException;
import com.group12.social_media_app.model.User;
import com.group12.social_media_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendRecommendationService {
    final int recommendMax = 4;

    private final UserRepository userRepository;

    @Autowired
    public FriendRecommendationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getRecommendedFriends(String email) {
        User currentUser = userRepository.findByEmail(email);
        if (currentUser == null) {
            throw new UserNotFoundException("User not found.");
        }

        List<Long> friendIds = currentUser.getFriends();
        List<User> allUsers = userRepository.findAll();

        // Filter out the current user and existing friends
        List<User> potentialFriends = allUsers.stream()
                .filter(user -> !user.getEmail().equals(email) && !friendIds.contains(user.getId()))
                .collect(Collectors.toList());

        // Prioritize users with the same tag
        List<User> sameTagUsers = potentialFriends.stream()
                .filter(user -> user.getTag() != null && user.getTag().equals(currentUser.getTag()))
                .collect(Collectors.toList());

        // Add other users who do not have the same tag
        List<User> otherUsers = potentialFriends.stream()
                .filter(user -> user.getTag() == null || !user.getTag().equals(currentUser.getTag()))
                .collect(Collectors.toList());

        // Combine lists, prioritizing same tag users and limiting to the max recommendations
        sameTagUsers.addAll(otherUsers);
        return sameTagUsers.stream().limit(recommendMax).collect(Collectors.toList());
    }
}
