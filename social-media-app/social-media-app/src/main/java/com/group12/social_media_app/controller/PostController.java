package com.group12.social_media_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.group12.social_media_app.model.Post;
import com.group12.social_media_app.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/users/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public List<Post> getPosts() {
        return postService.getAllPosts();
    }

    @PostMapping
    public Post addPost(@RequestBody Post post) {
        return postService.savePost(post);
    }
}
