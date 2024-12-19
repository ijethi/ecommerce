package com.group12.social_media_app.repository;

import com.group12.social_media_app.model.FriendRequest;
import com.group12.social_media_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByFriendAndAccepted(User friend, boolean accepted);
    Optional<FriendRequest> findByUserAndFriend(User user, User friend);
    void deleteByUserId(Long userId);
    void deleteByFriendId(Long friendId);
}
