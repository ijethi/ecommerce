package com.group12.social_media_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group12.social_media_app.config.SecurityConfig;
import com.group12.social_media_app.dto.*;
import com.group12.social_media_app.exception.UserAlreadyExistsException;
import com.group12.social_media_app.exception.UserNotFoundException;
import com.group12.social_media_app.model.User;
import com.group12.social_media_app.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testSignUpSuccess() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@dal.ca");
        signUpRequest.setPassword("Password1!");
        signUpRequest.setSecurityQuestion("First pet's name?");
        signUpRequest.setSecurityAnswer("Fluffy");

        when(userService.signUp(any(User.class))).thenReturn(new User());

        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@dal.ca\", \"password\":\"Password1!\", \"securityQuestion\":\"First pet's name?\", \"securityAnswer\":\"Fluffy\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully."));
    }

    @Test
    public void testSignUpInvalidEmail() throws Exception {
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@gmail.com\", \"password\":\"Password1!\", \"securityQuestion\":\"First pet's name?\", \"securityAnswer\":\"Fluffy\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid email domain. Only '@dal.ca' emails are allowed."));
    }

    @Test
    public void testLoginSuccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@dal.ca");
        loginRequest.setPassword("Password1!");

        User mockUser = new User("test@dal.ca", "Password1!", "First pet's name?", "Fluffy", "hiking");
        mockUser.setRole("USER");
        when(userService.login(anyString(), anyString())).thenReturn(mockUser);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@dal.ca\", \"password\":\"Password1!\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"email\":\"test@dal.ca\"}"));
    }

    @Test
    public void testLoginUserPending() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@dal.ca");
        loginRequest.setPassword("Password1!");

        User mockUser = new User("test@dal.ca", "Password1!", "First pet's name?", "Fluffy", "hiking");
        when(userService.login(anyString(), anyString())).thenReturn(mockUser);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@dal.ca\", \"password\":\"Password1!\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testLoginFailure() throws Exception {
        when(userService.login(anyString(), anyString())).thenThrow(new UserNotFoundException("Invalid email or password."));

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"wrong@dal.ca\", \"password\":\"wrongPassword\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testResetPasswordSuccess() throws Exception {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setEmail("test@dal.ca");
        resetPasswordRequest.setSecurityAnswer("Fluffy");
        resetPasswordRequest.setNewPassword("NewPassword1!");

        doNothing().when(userService).resetPassword(anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/users/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@dal.ca\", \"securityAnswer\":\"Fluffy\", \"newPassword\":\"NewPassword1!\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successfully."));
    }

    @Test
    public void testResetPasswordFailure() throws Exception {
        doThrow(new UserNotFoundException("Invalid email or security answer.")).when(userService).resetPassword(anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/users/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"wrong@dal.ca\", \"securityAnswer\":\"wrongAnswer\", \"newPassword\":\"NewPassword1!\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSendFriendRequest() throws Exception {
        // Prepare a friend request DTO
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setUserEmail("sender@dal.ca");
        friendRequest.setFriendEmail("receiver@dal.ca");

        mockMvc.perform(post("/api/users/send-friend-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userEmail\":\"sender@dal.ca\", \"friendEmail\":\"receiver@dal.ca\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Friend request sent successfully."));
    }

    @Test
    public void testChangeUserRole() throws Exception {
        ChangeRoleRequest changeRoleRequest = new ChangeRoleRequest("test@dal.ca", "ADMIN");

        doNothing().when(userService).changeUserRole("test@dal.ca", "ADMIN");

        mockMvc.perform(put("/api/users/change-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(changeRoleRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("User role updated successfully."));
    }


    @Test
    public void testChangeUserRoleUserNotFound() throws Exception {
        ChangeRoleRequest changeRoleRequest = new ChangeRoleRequest("unknown@dal.ca", "ADMIN");

        doThrow(new UserNotFoundException("User not found.")).when(userService).changeUserRole("unknown@dal.ca", "ADMIN");

        mockMvc.perform(put("/api/users/change-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(changeRoleRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found."));
    }
    @Test
    public void testAdminSignUp() throws Exception {
        AdminSignUpRequest adminSignUpRequest = new AdminSignUpRequest();
        adminSignUpRequest.setEmail("admin@dal.ca");
        adminSignUpRequest.setPassword("Admin123!");
        adminSignUpRequest.setSecurityQuestion("What is your favorite color?");
        adminSignUpRequest.setSecurityAnswer("Blue");
        adminSignUpRequest.setInterests("Tech");
        adminSignUpRequest.setSecretKey("CSCI3130");

        User adminUser = new User();
        adminUser.setEmail("admin@dal.ca");
        adminUser.setRole("ADMIN");

        when(userService.signUp(Mockito.any())).thenReturn(adminUser);

        mockMvc.perform(post("/api/users/admin-signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(adminSignUpRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Admin registered successfully."));
    }

    @Test
    public void testAddUserByAdmin() throws Exception {
        AddUserByAdminRequest addUserByAdminRequest = new AddUserByAdminRequest();
        addUserByAdminRequest.setEmail("test@dal.ca");
        addUserByAdminRequest.setPassword("Password1!");
        addUserByAdminRequest.setSecurityQuestion("First pet's name?");
        addUserByAdminRequest.setSecurityAnswer("Fluffy");


        mockMvc.perform(post("/api/users/add-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addUserByAdminRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User successfully created"));
    }

    @Test
    public void testAddUserByAdmin_UserAlreadyExists() throws Exception {
        AddUserByAdminRequest addUserByAdminRequest = new AddUserByAdminRequest();
        addUserByAdminRequest.setEmail("existing@dal.ca");
        addUserByAdminRequest.setPassword("Password1!");
        addUserByAdminRequest.setSecurityQuestion("First pet's name?");
        addUserByAdminRequest.setSecurityAnswer("Fluffy");

        doThrow(new UserAlreadyExistsException("User already exists with that email")).when(userService).addUserByAdmin(
                anyString(), anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/users/add-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addUserByAdminRequest)))
                .andExpect(status().isConflict())
                .andExpect(content().string("User already exists with that email"));
    }

    @Test
    public void testRemoveUser() throws Exception{

        User user = new User("test@dal.ca", "password", "What is your pet's name?", "Fluffy", "Reading");

        doNothing().when(userService).removeUser(user);

        mockMvc.perform(delete("/api/users/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("User successfully removed"));
    }

}
