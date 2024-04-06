package com.example.androidgpt_pro;

import static androidx.core.content.ContextCompat.startActivity;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.getIntents;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.notification.RunListener;

public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new
            ActivityScenarioRule<>((MainActivity.class));
    @Test
    public void testActivityChange() throws InterruptedException {
        // Tests if brought to Profile screen on login
        Intents.init();
        Thread.sleep(10000);
        intended(hasComponent(ProfileActivity.class.getName()));
        Intents.release();

    }
    @Test
    public void testProfileActivityEditButton() throws InterruptedException {
        Intents.init();
        Thread.sleep(10000);
        onView(withId(R.id.btn_edit_profile)).perform(click());
        intended(hasComponent(ProfileEditActivity.class.getName()));
        Intents.release();
    }

}
