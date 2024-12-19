package com.group12.social_media_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group12.social_media_app.model.Post;
import com.group12.social_media_app.model.User;
import com.group12.social_media_app.repository.PostRepository;
import com.group12.social_media_app.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public List<Post> getPostsForUser(String email) {
        User user = userRepository.findByEmail(email);
        List<Long> friendIds = user.getFriends();
        List<User> friends = friendIds.stream()
                .map(userRepository::findById)
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .collect(Collectors.toList());
        return postRepository.findAll().stream()
                .filter(post -> friends.contains(post.getUser()) || post.getUser().equals(user))
                .collect(Collectors.toList());
    }
}
