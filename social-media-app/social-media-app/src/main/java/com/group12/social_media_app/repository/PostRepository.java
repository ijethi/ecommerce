package com.group12.social_media_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group12.social_media_app.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
