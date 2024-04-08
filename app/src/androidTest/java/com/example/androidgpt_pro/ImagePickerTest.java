package com.example.androidgpt_pro;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.myhexaville.androidtests.MainActivity;
import com.myhexaville.androidtests.R;
import com.theartofdev.edmodo.cropper.CropImage;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.victoralbertos.device_animation_test_rule.DeviceAnimationTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.myhexaville.androidtests.common.Constants.IMAGE_URL;
import static com.myhexaville.androidtests.util.Matchers.hasDrawable;
import static com.myhexaville.androidtests.util.Matchers.withTag;
import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_EXTRA_RESULT;
import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_EXTRA_SOURCE;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.not;


// how to do thaaaaaaaaaaaaaaaaaat????????????
public class ImagePickerTest {
    @Rule
    public ActivityScenarioRule<EventCreateActivity> scenario = new
            ActivityScenarioRule<>((EventCreateActivity.class));

    @Before
    public void SetUp() {

    }
}
