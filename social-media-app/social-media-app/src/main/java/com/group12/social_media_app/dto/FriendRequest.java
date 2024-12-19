package com.group12.social_media_app.dto;

public class FriendRequest {

    private Long id;
    private String userEmail;
    private String friendEmail;

    public FriendRequest() {}

    public FriendRequest(Long id, String userEmail, String friendEmail) {
        this.id = id;
        this.userEmail = userEmail;
        this.friendEmail = friendEmail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public void setFriendEmail(String friendEmail) {
        this.friendEmail = friendEmail;
    }
}
