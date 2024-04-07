package com.example.androidgpt_pro;

public class Profile {
    private String profileId;
    private String name;
    private String email;
    private String imageUrl;

    public Profile() {
    }

    public Profile(String profileId, String name, String email, String imageUrl) {
        this.profileId = profileId;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getProfileId() {
        return profileId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Setters
    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
