package com.group12.social_media_app.dto;

public class ChangeRoleRequest {
    private String email;
    private String newRole;

    public ChangeRoleRequest(String email, String admin) {
        this.email = email;
        this.newRole = admin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewRole() {
        return newRole;
    }

    public void setNewRole(String newRole) {
        this.newRole = newRole;
    }
}