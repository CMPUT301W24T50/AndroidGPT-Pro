package com.example.androidgpt_pro;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MockProfileTest {

    private Profile mockProfile() {
        Profile mockProfile = new Profile("MockID","Mocker",null);
        return mockProfile;
    }

    @Test
    public void testProfileCreation() {
        Profile profile = mockProfile();

        assertEquals("MockID", profile.getProfileId());
        assertEquals("Mocker", profile.getName());
        assertNull(profile.getImageUri());
    }

}
