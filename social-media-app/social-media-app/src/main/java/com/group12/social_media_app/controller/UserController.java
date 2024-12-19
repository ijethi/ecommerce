package com.group12.social_media_app.controller;

import com.group12.social_media_app.dto.*;
import com.group12.social_media_app.exception.UserAlreadyExistsException;
import com.group12.social_media_app.exception.UserNotFoundException;
import com.group12.social_media_app.model.User;
import com.group12.social_media_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    final int minimumPasswordLen = 8;
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest signUpRequest) {
        if (!signUpRequest.getEmail().endsWith("@dal.ca")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid email domain. Only '@dal.ca' emails are allowed.");
        }

        if (!isValidPassword(signUpRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password does not meet the requirements.");
        }

        try {
            User user = new User(signUpRequest.getEmail(), signUpRequest.getPassword(),
                    signUpRequest.getSecurityQuestion(), signUpRequest.getSecurityAnswer(),
                    signUpRequest.getInterests());
            userService.signUp(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

            if (user.getRole().equals("PENDING")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        try {
            userService.resetPassword(resetPasswordRequest.getEmail(), resetPasswordRequest.getSecurityAnswer(),
                    resetPasswordRequest.getNewPassword());
            return ResponseEntity.ok("Password reset successfully.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    private boolean isValidPassword(String password) {
        return password.length() >= minimumPasswordLen &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*\\W.*");
    }

    @GetMapping("/profile/{email}")
    public ResponseEntity<User> getUserProfile(@PathVariable String email) {
        try {
            User user = userService.getUserProfile(email);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/profile/interests/{email}")
    public ResponseEntity<String> updateInterests(@PathVariable String email, @RequestBody String newInterests) {
        try {
            // Remove JSON formatting if necessary
            if (newInterests.startsWith("{") && newInterests.endsWith("}")) {
                newInterests = newInterests.substring(1, newInterests.length() - 1);
                if (newInterests.contains(":")) {
                    newInterests = newInterests.split(":")[1].replace("\"", "").trim();
                }
            }

            userService.updateInterests(email, newInterests);
            return ResponseEntity.ok("Interests updated successfully.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/profile/status/{email}")
    public ResponseEntity<String> updateStatus(@PathVariable String email, @RequestBody String newStatus) {
        try {
            if (newStatus.startsWith("{") && newStatus.endsWith("}")) {
                newStatus = newStatus.substring(1, newStatus.length() - 1);
                if (newStatus.contains(":")) {
                    newStatus = newStatus.split(":")[1].replace("\"", "").trim();
                }
            }

            userService.updateStatus(email, newStatus);
            return ResponseEntity.ok("Status updated successfully.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/send-friend-request")
    public ResponseEntity<String> sendFriendRequest(@RequestBody FriendRequest friendRequest) {
        try {
            userService.sendFriendRequest(friendRequest.getUserEmail(), friendRequest.getFriendEmail());
            return ResponseEntity.ok("Friend request sent successfully.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/friend-requests/{email}")
    public ResponseEntity<List<FriendRequest>> getFriendRequests(@PathVariable String email) {
        try {
            List<FriendRequest> friendRequests = userService.getFriendRequests(email).stream()
                    .map(request -> new FriendRequest(request.getId(), request.getUser().getEmail(),
                            request.getFriend().getEmail()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(friendRequests);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/accept-friend-request/{requestId}")
    public ResponseEntity<String> acceptFriendRequest(@PathVariable Long requestId) {
        userService.acceptFriendRequest(requestId);
        return ResponseEntity.ok("Friend request accepted.");
    }

    @GetMapping("/friends/{email}")
    public ResponseEntity<List<User>> getFriendsList(@PathVariable String email) {
        try {
            List<User> friends = userService.getFriendsList(email);
            return ResponseEntity.ok(friends);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/delete-friend/{email}/{friendEmail}")
    public ResponseEntity<String> deleteFriend(@PathVariable String email, @PathVariable String friendEmail) {
        try {
            userService.deleteFriend(email, friendEmail);
            return ResponseEntity.ok("Friend deleted successfully.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/change-role")
    public ResponseEntity<String> changeUserRole(@RequestBody ChangeRoleRequest changeRoleRequest) {
        try {
            userService.changeUserRole(changeRoleRequest.getEmail(), changeRoleRequest.getNewRole());
            return ResponseEntity.ok("User role updated successfully.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/admin-signup")
    public ResponseEntity<String> adminSignUp(@RequestBody AdminSignUpRequest adminSignUpRequest) {
        if (!adminSignUpRequest.getEmail().endsWith("@dal.ca")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid email domain. Only '@dal.ca' emails are allowed.");
        }

        if (!isValidPassword(adminSignUpRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password does not meet the requirements.");
        }

        try {
            User user = new User(adminSignUpRequest.getEmail(), adminSignUpRequest.getPassword(),
                    adminSignUpRequest.getSecurityQuestion(), adminSignUpRequest.getSecurityAnswer(),
                    adminSignUpRequest.getInterests());
            userService.signUp(user, adminSignUpRequest.getSecretKey());
            return ResponseEntity.status(HttpStatus.CREATED).body("Admin registered successfully.");
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sign up failed.");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/add-user")
    public ResponseEntity<String> addUserByAdmin(@RequestBody AddUserByAdminRequest addUserByAdminRequest) {
        if (!addUserByAdminRequest.getEmail().endsWith("@dal.ca")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid email domain. Only '@dal.ca' emails are allowed.");
        }
        if (!isValidPassword(addUserByAdminRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password does not meet the requirements.");
        }
        try {
            User user = userService.addUserByAdmin(
                    addUserByAdminRequest.getEmail(),
                    addUserByAdminRequest.getPassword(),
                    addUserByAdminRequest.getSecurityQuestion(),
                    addUserByAdminRequest.getSecurityAnswer());
            return new ResponseEntity<>("User successfully created", HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>("User already exists with that email", HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeUser(@RequestBody User user) {
        try {
            userService.removeUser(user);
            return ResponseEntity.status(HttpStatus.OK).body("User successfully removed");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/profile/tag/{email}")
    public ResponseEntity<String> updateTag(@PathVariable String email, @RequestBody String newTag) {
        try {
            if (newTag.startsWith("{") && newTag.endsWith("}")) {
                newTag = newTag.substring(1, newTag.length() - 1);
                if (newTag.contains(":")) {
                    newTag = newTag.split(":")[1].replace("\"", "").trim();
                }
            }

            userService.updateTag(email, newTag);
            return ResponseEntity.ok("Tag updated successfully.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String query) {
        List<User> users = userService.searchUsers(query);
        return ResponseEntity.ok(users);
    }

    @PutMapping("activate/{email}")
    public ResponseEntity<String> activateUser(@PathVariable String email) {
        try {
            userService.activateUser(email);
            return ResponseEntity.ok("User activation Successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/reject-friend-request/{requestId}")
    public ResponseEntity<String> rejectFriendRequest(@PathVariable Long requestId) {
        userService.rejectFriendRequest(requestId);
        return ResponseEntity.ok("Friend request rejected.");
    }
    @DeleteMapping("reject/{email}")
    public ResponseEntity<String> rejectUser(@PathVariable String email) {
        try {
            userService.rejectUser(email);
            return ResponseEntity.ok("User rejected and deleted successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
