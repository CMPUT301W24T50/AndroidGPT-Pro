package com.example.androidgpt_pro;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


import java.util.Date;

public class MockNotificationTest {
    private Notification mockNotification() {
        Notification mockNotification = new Notification("MockID", "MockName",
                "MockMessage");
        return mockNotification;
    }

    @Test
    public void TestNotificationCreation() {
        Notification notification = mockNotification();

        assertEquals("MockID", notification.getEventID());
        assertEquals("MockName", notification.getEventName());
        assertEquals("MockMessage", notification.getNotificationText());
    }
}
