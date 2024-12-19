package com.group12.social_media_app.service;

import com.group12.social_media_app.model.FriendRequest;
import com.group12.social_media_app.model.User;
import com.group12.social_media_app.repository.FriendRequestRepository;
import com.group12.social_media_app.repository.UserRepository;
import com.group12.social_media_app.exception.UserAlreadyExistsException;
import com.group12.social_media_app.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

@Service
public class UserService {
    final int minimumPasswordLen = 8;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    private static final String ADMIN_SECRET_KEY = "CSCI3130";

    public User signUp(User user) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists.");
        }
        return userRepository.save(user);
    }

    public User login(String email, String password) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            throw new UserNotFoundException("Invalid email or password.");
        }
        return user;
    }

    public void resetPassword(String email, String securityAnswer, String newPassword) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null || !user.getSecurityAnswer().equals(securityAnswer)) {
            throw new UserNotFoundException("Invalid email or security answer.");
        }
        if (!isValidPassword(newPassword)) {
            throw new IllegalArgumentException("Password does not meet the requirements.");
        }
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    private boolean isValidPassword(String password) {
        return password.length() >= minimumPasswordLen &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*\\W.*");
    }

    public User getUserProfile(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found.");
        }
        return user;
    }

    public void updateInterests(String email, String newInterests) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found.");
        }
        user.setInterests(newInterests);
        userRepository.save(user);
    }

    public void updateStatus(String email, String newStatus) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found.");
        }
        user.setStatus(newStatus);
        userRepository.save(user);
    }

    public void sendFriendRequest(String userEmail, String friendEmail) throws UserNotFoundException {
        User user = userRepository.findByEmail(userEmail);
        User friend = userRepository.findByEmail(friendEmail);
        if (user == null || friend == null) {
            throw new UserNotFoundException("User or friend not found.");
        }
        if (friendRequestRepository.findByUserAndFriend(user, friend).isPresent()) {
            throw new UserNotFoundException("Friend request already exists.");
        }
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setUser(user);
        friendRequest.setFriend(friend);
        friendRequest.setAccepted(false);

        friendRequestRepository.save(friendRequest);
    }

    public List<FriendRequest> getFriendRequests(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found.");
        }
        return friendRequestRepository.findByFriendAndAccepted(user, false);
    }

    public void acceptFriendRequest(Long requestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found."));
        friendRequest.setAccepted(true);

        User user = friendRequest.getUser();
        User friend = friendRequest.getFriend();

        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());

        userRepository.save(user);
        userRepository.save(friend);

        friendRequestRepository.save(friendRequest);
    }

    public List<User> getFriendsList(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found.");
        }
        List<User> friends = new ArrayList<>();
        for (Long friendId : user.getFriends()) {
            User friend = userRepository.findById(friendId).orElse(null);
            if (friend != null) {
                friends.add(friend);
            }
        }
        return friends;
    }

    public void deleteFriend(String email, String friendEmail) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        User friend = userRepository.findByEmail(friendEmail);
        if (user == null || friend == null) {
            throw new UserNotFoundException("User or friend not found.");
        }

        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());

        userRepository.save(user);
        userRepository.save(friend);
    }

    public void changeUserRole(String email, String newRole) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found.");
        }
        user.setRole(newRole);
        userRepository.save(user);
    }

    // admin sign up
    public User signUp(User user, String secretKey) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists.");
        }
        if (ADMIN_SECRET_KEY.equals(secretKey)) {
            user.setRole("ADMIN");
        } else {
            throw new RuntimeException("Invalid secret key for admin sign up.");
        }
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User addUserByAdmin(String email, String password, String securityQuestion, String securityAnswer) {

        if (userRepository.findByEmail(email) != null) {
            throw new UserAlreadyExistsException("User already exists with that email");
        }

        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Password does not meet the requirements.");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setSecurityQuestion(securityQuestion);
        user.setSecurityAnswer(securityAnswer);
        user.setRole("USER");
        return userRepository.save(user);
    }

    @Transactional
    public void removeUser(User user) throws UserNotFoundException, IllegalArgumentException {
        User target = userRepository.findByEmail(user.getEmail());
        if (target == null) {
            throw new UserNotFoundException("User not found.");
        }

        friendRequestRepository.deleteByUserId(target.getId());
        friendRequestRepository.deleteByFriendId(target.getId());

        List<User> allUsers = userRepository.findAll();
        for (User u : allUsers) {
            if (u.getFriends().contains(target.getId())) {
                u.getFriends().remove(target.getId());
                userRepository.save(u);
            }
        }//

        userRepository.delete(target);
    }

    public void updateTag(String email, String newTag) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found.");
        }
        user.setTag(newTag);
        userRepository.save(user);
    }

    public List<User> searchUsers(String query) {
        return userRepository.searchUsers(query);
    }

    public void activateUser(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        user.setRole("USER");
        userRepository.save(user);
    }
    public void rejectUser(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        userRepository.delete(user);
    }
    public void rejectFriendRequest(Long requestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found."));
        friendRequestRepository.delete(friendRequest);
    }
}
