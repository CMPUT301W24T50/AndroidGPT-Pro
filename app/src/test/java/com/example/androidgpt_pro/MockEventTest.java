package com.example.androidgpt_pro;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


import java.util.Date;
public class MockEventTest {
    private Event mockEvent() {
        Event mockEvent = new Event("MockID", "MockEvent","MockCity",
                "MockProvince", "0","0",null);
        return mockEvent;
    }

    @Test
    public void testEventCreation() {
        Event event = mockEvent();

        assertEquals("MockID", event.getEventID());
        assertEquals("MockEvent", event.getEventName());
        assertEquals("MockCity", event.getEventLocationCity());
        assertEquals("MockProvince", event.getEventLocationProvince());
        assertEquals("0", event.getEventTime());
        assertEquals("0", event.getEventDate());
        assertNull(event.getEventImageUri());
    }
}
