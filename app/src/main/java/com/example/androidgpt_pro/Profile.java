package com.example.androidgpt_pro;

public class Profile {
    private String profileId;
    private String name;
    private String email;
    private String imageUrl;

    public Profile() {
    }

    /**
     * this is the constructor of the profile
     * @param profileId
     * @param name
     * @param email
     * @param imageUrl
     */
    public Profile(String profileId, String name, String email, String imageUrl) {
        this.profileId = profileId;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    /**
     * this is the getter of the profile id
     * @return profileId
     */
    public String getProfileId() {
        return profileId;
    }

    /**
     * this is the getter of the profile name
     * @return profile Name
     */
    public String getName() {
        return name;
    }

    /**
     * this is the getter of the profile email
     * @return profile email
     */
    public String getEmail() {
        return email;
    }

    /**
     * this is the setter of the image url
     * @return image url
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * this is the setter of the profile id
     * @param profileId
     */
    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    /**
     * this is the setter of the profile name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * this is the setter of the profile email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * this is the setter of the profile image url
     * @param imageUrl
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
