package com.group12.social_media_app.repository;

import com.group12.social_media_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.email LIKE %:query% OR u.tag LIKE %:query%")
    List<User> searchUsers(@Param("query") String query);
}
