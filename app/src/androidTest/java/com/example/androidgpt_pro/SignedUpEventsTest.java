package com.example.androidgpt_pro;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.google.common.reflect.Reflection.initialize;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SignedUpEventsTest {

    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new
            ActivityScenarioRule<>(MainActivity.class);

    public void initialize() throws InterruptedException {
        Intents.init();
        Thread.sleep(3000);
    }

    @Test
    public void testSignedUpEventsActivityChange() throws InterruptedException {
        initialize();
        onView(withId(R.id.btn_sign_up_event)).perform(click());
        intended(hasComponent(EventSignUpActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testSignedUpEventsDisplayed() {
        // Simulate creating and signing up for test events
        Intents.init();
        // Navigate to EventSignUpActivity
        onView(withId(R.id.btn_sign_up_event)).perform(click());
        // Verify if the EventSignUpActivity is displayed
        intended(hasComponent(EventSignUpActivity.class.getName()));


        onView(withText("test")).check(matches(isDisplayed()));
        //onView(withText("Event 2")).check(matches(isDisplayed()));
        //onView(withText("Event 3")).check(matches(isDisplayed()));
    }
}
