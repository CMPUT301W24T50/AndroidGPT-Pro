package com.example.androidgpt_pro;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class NavigationBarTest {

    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new
            ActivityScenarioRule<>((MainActivity.class));

    public void initialize() throws InterruptedException {
        Intents.init();
        Thread.sleep(8000);
    }
    @Test
    public void testEventsActivityChange() throws InterruptedException {
        initialize();
        onView(withId(R.id.events_tab)).perform(click());
        intended(hasComponent(EventAllActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testQRScannerActivityChange() throws InterruptedException {
        initialize();
        onView(withId(R.id.qr_scanner_tab)).perform(click());
        intended(hasComponent(QRScannerActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testProfileTabActivityChange() throws InterruptedException {
        initialize();
        onView(withId(R.id.qr_scanner_tab)).perform(click());
        onView(withId(R.id.profile_tab)).perform(click());
        intended(hasComponent(ProfileActivity.class.getName()));
        Intents.release();
    }
}
