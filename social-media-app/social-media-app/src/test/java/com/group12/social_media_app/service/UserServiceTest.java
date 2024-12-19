package com.group12.social_media_app.service;

import com.group12.social_media_app.exception.UserAlreadyExistsException;
import com.group12.social_media_app.exception.UserNotFoundException;
import com.group12.social_media_app.model.FriendRequest;
import com.group12.social_media_app.model.User;
import com.group12.social_media_app.repository.FriendRequestRepository;
import com.group12.social_media_app.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignUp() throws UserAlreadyExistsException {
        User user = new User("test@dal.ca", "Password#1", "First pet's name?", "Fluffy", "Reading");
        when(userRepository.findByEmail("test@dal.ca")).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.signUp(user);
        assertEquals(user, result);
        verify(userRepository).save(user);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testSignUpUserAlreadyExists() throws UserAlreadyExistsException {
        User existingUser = new User("test@dal.ca", "Password#1", "First pet's name?", "Fluffy", "Reading");
        when(userRepository.findByEmail("test@dal.ca")).thenReturn(existingUser);

        User user = new User("test@dal.ca", "Password#1", "First pet's name?", "Fluffy", "Reading");
        userService.signUp(user);
    }

    @Test
    public void testLogin() throws UserNotFoundException {
        User user = new User("test@dal.ca", "password", "What is your pet's name?", "Fluffy", "Reading");
        when(userRepository.findByEmail("test@dal.ca")).thenReturn(user);

        User result = userService.login("test@dal.ca", "password");
        assertEquals(user, result);
    }

    @Test(expected = UserNotFoundException.class)
    public void testLoginInvalidEmail() throws UserNotFoundException {
        when(userRepository.findByEmail("unknown@dal.ca")).thenReturn(null);

        userService.login("unknown@dal.ca", "password");
    }

    @Test(expected = UserNotFoundException.class)
    public void testLoginInvalidPassword() throws UserNotFoundException {
        User user = new User("test@dal.ca", "password", "What is your pet's name?", "Fluffy", "Reading");
        when(userRepository.findByEmail("test@dal.ca")).thenReturn(user);

        userService.login("test@dal.ca", "wrongPassword");
    }

    @Test
    public void testResetPassword() throws UserNotFoundException {
        User user = new User("test@dal.ca", "password", "What is your pet's name?", "Fluffy", "Reading");
        when(userRepository.findByEmail("test@dal.ca")).thenReturn(user);

        userService.resetPassword("test@dal.ca", "Fluffy", "NewPassword#1");
        assertEquals("NewPassword#1", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test(expected = UserNotFoundException.class)
    public void testResetPasswordInvalidSecurityAnswer() throws UserNotFoundException {
        User user = new User("test@dal.ca", "password", "What is your pet's name?", "Fluffy", "Reading");
        when(userRepository.findByEmail("test@dal.ca")).thenReturn(user);

        userService.resetPassword("test@dal.ca", "WrongAnswer", "NewPassword#1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetPasswordInvalidPassword() throws UserNotFoundException {
        User user = new User("test@dal.ca", "password", "What is your pet's name?", "Fluffy", "Reading");
        when(userRepository.findByEmail("test@dal.ca")).thenReturn(user);

        userService.resetPassword("test@dal.ca", "Fluffy", "short");
    }

    @Test
    public void testGetUserProfile() throws UserNotFoundException {
        User user = new User("test@dal.ca", "password", "What is your pet's name?", "Fluffy", "Reading");
        when(userRepository.findByEmail("test@dal.ca")).thenReturn(user);

        User result = userService.getUserProfile("test@dal.ca");
        assertEquals(user, result);
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetUserProfileUserNotFound() throws UserNotFoundException {
        when(userRepository.findByEmail("unknown@dal.ca")).thenReturn(null);

        userService.getUserProfile("unknown@dal.ca");
    }

    @Test
    public void testUpdateInterests() throws UserNotFoundException {
        User user = new User("test@dal.ca", "password", "What is your pet's name?", "Fluffy", "Reading");
        when(userRepository.findByEmail("test@dal.ca")).thenReturn(user);

        userService.updateInterests("test@dal.ca", "Coding");
        assertEquals("Coding", user.getInterests());
        verify(userRepository).save(user);
    }

    @Test(expected = UserNotFoundException.class)
    public void testUpdateInterestsUserNotFound() throws UserNotFoundException {
        when(userRepository.findByEmail("unknown@dal.ca")).thenReturn(null);

        userService.updateInterests("unknown@dal.ca", "Coding");
    }

    @Test
    public void testUpdateStatus() throws UserNotFoundException {
        User user = new User("test@dal.ca", "password", "What is your pet's name?", "Fluffy", "Reading");
        when(userRepository.findByEmail("test@dal.ca")).thenReturn(user);

        userService.updateStatus("test@dal.ca", "Online");
        assertEquals("Online", user.getStatus());
        verify(userRepository).save(user);
    }

    @Test(expected = UserNotFoundException.class)
    public void testUpdateStatusUserNotFound() throws UserNotFoundException {
        when(userRepository.findByEmail("unknown@dal.ca")).thenReturn(null);

        userService.updateStatus("unknown@dal.ca", "Online");
    }

    @Test
    public void testSendFriendRequest() throws UserNotFoundException {
        User user = new User("user@dal.ca", "password", "What is your pet's name?", "Fluffy", "Reading");
        User friend = new User("friend@dal.ca", "password", "What is your pet's name?", "Buddy", "Traveling");
        when(userRepository.findByEmail("user@dal.ca")).thenReturn(user);
        when(userRepository.findByEmail("friend@dal.ca")).thenReturn(friend);
        when(friendRequestRepository.findByUserAndFriend(user, friend)).thenReturn(Optional.empty());

        userService.sendFriendRequest("user@dal.ca", "friend@dal.ca");
        verify(friendRequestRepository).save(any(FriendRequest.class));
    }

    @Test(expected = UserNotFoundException.class)
    public void testSendFriendRequestUserNotFound() throws UserNotFoundException {
        when(userRepository.findByEmail("user@dal.ca")).thenReturn(null);

        userService.sendFriendRequest("user@dal.ca", "friend@dal.ca");
    }

    @Test(expected = UserNotFoundException.class)
    public void testSendFriendRequestFriendNotFound() throws UserNotFoundException {
        User user = new User("user@dal.ca", "password", "What is your pet's name?", "Fluffy", "Reading");
        when(userRepository.findByEmail("user@dal.ca")).thenReturn(user);
        when(userRepository.findByEmail("friend@dal.ca")).thenReturn(null);

        userService.sendFriendRequest("user@dal.ca", "friend@dal.ca");
    }

    @Test
    public void testGetFriendRequests() throws UserNotFoundException {
        User user = new User("user@dal.ca", "password", "What is your pet's name?", "Fluffy", "Reading");
        FriendRequest request = new FriendRequest();
        request.setUser(user);
        when(userRepository.findByEmail("user@dal.ca")).thenReturn(user);
        when(friendRequestRepository.findByFriendAndAccepted(user, false)).thenReturn(Arrays.asList(request));

        List<FriendRequest> result = userService.getFriendRequests("user@dal.ca");
        assertEquals(1, result.size());
        assertEquals(request, result.get(0));
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetFriendRequestsUserNotFound() throws UserNotFoundException {
        when(userRepository.findByEmail("unknown@dal.ca")).thenReturn(null);

        userService.getFriendRequests("unknown@dal.ca");
    }

    @Test
    public void testAcceptFriendRequest() {
        User user = new User("user@dal.ca", "password", "What is your pet's name?", "Fluffy", "Reading");
        User friend = new User("friend@dal.ca", "password", "What is your pet's name?", "Buddy", "Traveling");
        user.setFriends(new ArrayList<>());
        friend.setFriends(new ArrayList<>());
        FriendRequest request = new FriendRequest();
        request.setId(1L);
        request.setUser(user);
        request.setFriend(friend);
        when(friendRequestRepository.findById(1L)).thenReturn(Optional.of(request));

        userService.acceptFriendRequest(1L);
        assertTrue(request.isAccepted());
        assertTrue(user.getFriends().contains(friend.getId()));
        assertTrue(friend.getFriends().contains(user.getId()));
        verify(userRepository, times(2)).save(any(User.class));
        verify(friendRequestRepository).save(request);
    }

    @Test
    public void testGetFriendsList() throws UserNotFoundException {
        User user = new User("user@dal.ca", "password", "What is your pet's name?", "Fluffy", "Reading");
        user.setFriends(Arrays.asList(1L, 2L));
        User friend1 = new User();
        friend1.setId(1L);
        User friend2 = new User();
        friend2.setId(2L);
        when(userRepository.findByEmail("user@dal.ca")).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(friend1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(friend2));

        List<User> result = userService.getFriendsList("user@dal.ca");
        assertEquals(2, result.size());
        assertTrue(result.contains(friend1));
        assertTrue(result.contains(friend2));
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetFriendsListUserNotFound() throws UserNotFoundException {
        when(userRepository.findByEmail("unknown@dal.ca")).thenReturn(null);

        userService.getFriendsList("unknown@dal.ca");
    }

    @Test
    public void testDeleteFriend() throws UserNotFoundException {
        User user = new User("user@dal.ca", "password", "What is your pet's name?", "Fluffy", "Reading");
        User friend = new User("friend@dal.ca", "password", "What is your pet's name?", "Buddy", "Traveling");
        user.setFriends(new ArrayList<>(Collections.singletonList(friend.getId())));
        friend.setFriends(new ArrayList<>(Collections.singletonList(user.getId())));
        when(userRepository.findByEmail("user@dal.ca")).thenReturn(user);
        when(userRepository.findByEmail("friend@dal.ca")).thenReturn(friend);

        userService.deleteFriend("user@dal.ca", "friend@dal.ca");
        assertFalse(user.getFriends().contains(friend.getId()));
        assertFalse(friend.getFriends().contains(user.getId()));
        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    public void testChangeUserRole() throws UserNotFoundException {
        User user = new User("test@dal.ca", "password", "What is your pet's name?", "Fluffy", "Reading");
        user.setRole("USER");
        when(userRepository.findByEmail("test@dal.ca")).thenReturn(user);

        userService.changeUserRole("test@dal.ca", "ADMIN");
        assertEquals("ADMIN", user.getRole());
        verify(userRepository).save(user);
    }

    @Test(expected = UserNotFoundException.class)
    public void testChangeUserRoleUserNotFound() throws UserNotFoundException {
        when(userRepository.findByEmail("unknown@dal.ca")).thenReturn(null);

        userService.changeUserRole("unknown@dal.ca", "ADMIN");
    }

    @Test
    public void testAdminSignUp() throws UserAlreadyExistsException {
        User user = new User("admin@dal.ca", "Password#1", "First pet's name?", "Fluffy", "Tech");
        when(userRepository.findByEmail("admin@dal.ca")).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.signUp(user, "CSCI3130");
        assertEquals("ADMIN", user.getRole());
        assertEquals(user, result);
        verify(userRepository).save(user);
    }

    @Test(expected = RuntimeException.class)
    public void testAdminSignUpInvalidSecretKey() throws UserAlreadyExistsException {
        User user = new User("admin@dal.ca", "Password#1", "First pet's name?", "Fluffy", "Tech");
        when(userRepository.findByEmail("admin@dal.ca")).thenReturn(null);

        userService.signUp(user, "WrongKey");
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User("user1@dal.ca", "password", "What is your pet's name?", "Fluffy", "Reading");
        User user2 = new User("user2@dal.ca", "password", "What is your pet's name?", "Buddy", "Traveling");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> result = userService.getAllUsers();
        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
    }

    @Test
    public void testRemoveUser() throws UserNotFoundException {
        User user = new User("test@dal.ca", "password", "What is your pet's name?", "Fluffy", "Reading");
        when(userRepository.findByEmail("test@dal.ca")).thenReturn(user);

        userService.removeUser(user);
        verify(userRepository).delete(user);
    }

    @Test(expected = UserNotFoundException.class)
    public void testRemoveUserNotFound() throws UserNotFoundException {
        User user = new User("unknown@dal.ca", "password", "What is your pet's name?", "Fluffy", "Reading");
        when(userRepository.findByEmail("unknown@dal.ca")).thenReturn(null);

        userService.removeUser(user);
    }

    @Test
    public void testUpdateTag() throws UserNotFoundException {
        User user = new User("test@dal.ca", "password", "What is your pet's name?", "Fluffy", "Reading");
        when(userRepository.findByEmail("test@dal.ca")).thenReturn(user);

        userService.updateTag("test@dal.ca", "New Tag");
        assertEquals("New Tag", user.getTag());
        verify(userRepository).save(user);
    }

    @Test(expected = UserNotFoundException.class)
    public void testUpdateTagUserNotFound() throws UserNotFoundException {
        when(userRepository.findByEmail("unknown@dal.ca")).thenReturn(null);

        userService.updateTag("unknown@dal.ca", "New Tag");
    }

    @Test
    public void testActivateUser() throws UserNotFoundException {
        User user = new User("test@dal.ca", "password", "What is your pet's name?", "Fluffy", "Reading");
        when(userRepository.findByEmail("test@dal.ca")).thenReturn(user);

        userService.activateUser("test@dal.ca");
        assertEquals("USER", user.getRole());
        verify(userRepository).save(user);
    }

    @Test(expected = UserNotFoundException.class)
    public void testActivateUserUserNotFound() throws UserNotFoundException {
        when(userRepository.findByEmail("unknown@dal.ca")).thenReturn(null);

        userService.activateUser("unknown@dal.ca");
    }

    @Test
    public void testRejectFriendRequest() {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setId(1L);
        when(friendRequestRepository.findById(1L)).thenReturn(Optional.of(friendRequest));

        userService.rejectFriendRequest(1L);
        verify(friendRequestRepository).delete(friendRequest);
    }

    @Test(expected = RuntimeException.class)
    public void testRejectFriendRequestNotFound() {
        when(friendRequestRepository.findById(1L)).thenReturn(Optional.empty());

        userService.rejectFriendRequest(1L);
    }
}
