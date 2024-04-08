package com.example.androidgpt_pro;

import android.net.Uri;

/**
 * This class represents a user profile.
 */
public class Profile {
    private String profileId;
    private String name;
    private Uri imageUri;


    /**
     * this is the constructor of the profile
     * @param profileId
     * @param name
     * @param imageUri
     */
    public Profile(String profileId, String name, Uri imageUri) {
        this.profileId = profileId;
        this.name = name;
        this.imageUri = imageUri;
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
     * this is the setter of the image url
     * @return image url
     */
    public Uri getImageUri() {
        return imageUri;
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
     * this is the setter of the profile image url
     * @param imageUri
     */
    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
