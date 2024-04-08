package com.example.androidgpt_pro;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.content.Intent;
import android.support.test.espresso.contrib.PickerActions;
import android.view.View;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class MyEventsTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new
            ActivityScenarioRule<>(MainActivity.class);

    public void initialize() throws InterruptedException {
        Intents.init();
        Thread.sleep(3000);
        onView(withId(R.id.btn_my_event)).perform(click());
        Thread.sleep(1500);
    }

    @Test
    public void testEventCreateActivitySwitch() throws InterruptedException {
        initialize();

        onView(withId(R.id.event_create_btn)).perform(click());
        intended(hasComponent(EventCreateActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testEventSelection() throws InterruptedException {
        initialize();
        onView(withId(R.id.event_list_view)).perform(click());
        intended(hasComponent(EventMyDetailActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testEventSignedUpAttendees() throws InterruptedException {
        initialize();
        onView(withId(R.id.event_list_view)).perform(click());
        onView(withId(R.id.organizer_signed_up_event_attendee)).perform(click());
        intended(hasComponent(SignedUpAttendee.class.getName()));
        Intents.release();
    }

    @Test
    public void testEventCheckedInAttendees() throws InterruptedException {
        initialize();
        onView(withId(R.id.event_list_view)).perform(click());
        onView(withId(R.id.organizer_event_attendee)).perform(click());
        intended(hasComponent(AttendeeCountActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testEventCheckInMap() throws InterruptedException {
        initialize();
        onView(withId(R.id.event_list_view)).perform(click());
        onView(withId(R.id.organizer_event_map_btn)).perform(click());
        intended(hasComponent(EventCheckInMap.class.getName()));
        Intents.release();
    }

//    @Test
//    public void testCreateEvent() throws InterruptedException {
//        initialize();
//        onView(withId(R.id.event_create_btn)).perform(click());
//        onView(withId(R.id.edit_event_name)).perform(typeText("Test Event"))
//                .perform(closeSoftKeyboard());
//        onView(withId(R.id.edit_event_date)).perform((ViewAction) PickerActions.setDate(2025, 1, 1));
//        onView(withId(R.id.edit_event_time)).perform((ViewAction) PickerActions.setTime(5, 0));
//        onView(withId(R.id.edit_street_address)).perform(typeText("123 Test St"));
//        onView(withId(R.id.edit_city_address)).perform(typeText("Test City"));
//        onView(withId(R.id.edit_province_address)).perform(typeText("Alberta"));
//        onView(withId(R.id.geo_location_switch)).perform(click());
//    }
}
