package com.group12.social_media_app.exception;

public class UserStillPendingException extends RuntimeException {
    public UserStillPendingException(String message) {
        super(message);
    }
}
