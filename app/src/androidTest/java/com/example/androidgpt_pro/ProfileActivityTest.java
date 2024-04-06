package com.example.androidgpt_pro;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class ProfileActivityTest {
    @Rule
    public ActivityScenarioRule<ProfileActivity> scenario = new
            ActivityScenarioRule<>(ProfileActivity.class);
//    Intent intent = new Intent(MainActivity.class.newInstance(), ProfileActivity.class);


//    intent.putExtra("userID", "123");

    @Test
    public void testProfileName(){
        onView(withText("NewUser")).check(matches(isDisplayed()));
    }
}
