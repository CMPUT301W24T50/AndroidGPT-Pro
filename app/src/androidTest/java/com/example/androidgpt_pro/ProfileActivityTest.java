package com.example.androidgpt_pro;

import static android.app.PendingIntent.getActivity;
import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class ProfileActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new
            ActivityScenarioRule<>(MainActivity.class);

    public void initialize() throws InterruptedException {
        Intents.init();
        Thread.sleep(8000);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Test
    public void testProfileActivityEditButton() throws InterruptedException {
        initialize();
        onView(withId(R.id.btn_edit_profile)).perform(click());
        intended(hasComponent(ProfileEditActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testEditProfileInfo() throws InterruptedException {
        initialize();
        onView(withId(R.id.btn_edit_profile)).perform(click());
        onView(withId(R.id.edit_text_edit_profile_name))
                .perform(ViewActions.clearText())
                .perform(ViewActions.typeText("Test"));
        onView(withId(R.id.edit_text_edit_phone_number))
                .perform(ViewActions.clearText())
                .perform(ViewActions.typeText("1234567890"));
        onView(withId(R.id.edit_text_edit_email))
                .perform(ViewActions.clearText())
                .perform(ViewActions.typeText("example@test.com"))
                .perform(closeSoftKeyboard());

        onView(withId(R.id.button_save_profile)).perform(click());
        onView(withId(R.id.text_profile_name)).check(matches(withText("Test")));
        onView(withText("1234567890")).check(matches(isDisplayed()));
        onView(withText("example@test.com")).check(matches(isDisplayed()));
        Intents.release();
    }

    @Test
    public void testMyEventsActivityChange() throws InterruptedException {
        initialize();
        onView(withId(R.id.btn_my_event)).perform(click());
        intended(hasComponent(EventMyActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testSignedUpActivityChange() throws InterruptedException {
        initialize();
        onView(withId(R.id.btn_sign_up_event)).perform(click());
        intended(hasComponent(EventSignUpActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testNotificationActivityChange() throws InterruptedException {
        initialize();
        onView(withId(R.id.notification_icon)).perform(click());
        intended(hasComponent(Notifications.class.getName()));
        Intents.release();
    }

}
