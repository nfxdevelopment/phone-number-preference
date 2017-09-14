package com.nfx.android.phonenumberpreferencesample;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.nfx.android.phonenumberpreference.PhoneNumberPreferenceCompat;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class PhoneNumberActivityTest {

    @Rule
    public ActivityTestRule<PhoneNumberActivityCompat> mActivityTestRule =
            new ActivityTestRule<>(PhoneNumberActivityCompat.class);

    @Test
    public void checkListIsPopulated() {
        onView(allOf(withId(android.R.id.title), withText("Phone Number"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.list),
                                        0),
                                0),
                        isDisplayed()))
                .check(matches(withText("Phone Number")));

        onView(allOf(withId(android.R.id.summary), withText("This is a phone number"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.list),
                                        0),
                                1),
                        isDisplayed()))
                .check(matches(withText("This is a phone number")));
    }

    @Test
    public void testUkNumber() {
        String ukNumber = "+44 7777 777777";
        onView(allOf(childAtPosition(
                        allOf(withId(R.id.list), isEnabled()),
                        0),
                        isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.phone_number_edit_text), isDisplayed())).perform(click());

        onView(allOf(withId(R.id.phone_number_edit_text), isDisplayed()))
                .perform(replaceText(ukNumber), closeSoftKeyboard());

        onView(allOf(withId(R.id.btn_apply), withText("apply"), isDisplayed())).perform(click());

        onView(allOf(withId(R.id.phone_number),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.list),
                                        0),
                                2),
                        isDisplayed()))
                .check(matches(withText(ukNumber)));
    }

    @Test
    public void testUsLocalNumber() {
        String ukNumber = "+1 202-555-0144";
        onView(allOf(childAtPosition(
                allOf(withId(R.id.list), isEnabled()),
                0),
                isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.phone_number_edit_text), isDisplayed())).perform(click());

        onView(allOf(withId(R.id.phone_number_edit_text), isDisplayed()))
                .perform(replaceText(ukNumber), closeSoftKeyboard());

        onView(allOf(withId(R.id.btn_apply), withText("apply"), isDisplayed())).perform(click());

        onView(allOf(withId(R.id.phone_number),
                childAtPosition(
                        childAtPosition(
                                withId(R.id.list),
                                0),
                        2),
                isDisplayed()))
                .check(matches(withText(ukNumber)));
    }

    @Test
    public void testNorwegianNumber() {
        String phoneNumber = "+47 55 55 55 55";
        onView(allOf(childAtPosition(
                allOf(withId(R.id.list), isEnabled()),
                0),
                isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.phone_number_edit_text), isDisplayed())).perform(click());

        onView(allOf(withId(R.id.phone_number_edit_text), isDisplayed()))
                .perform(replaceText(phoneNumber), closeSoftKeyboard());

        onView(allOf(withId(R.id.btn_apply), withText("apply"), isDisplayed())).perform(click());

        onView(allOf(withId(R.id.phone_number),
                childAtPosition(
                        childAtPosition(
                                withId(R.id.list),
                                0),
                        2),
                isDisplayed()))
                .check(matches(withText(phoneNumber)));
    }

    @Test
    public void testNorwegianMobileNumber() {
        String phoneNumber = "+47 437 78 879";
        onView(allOf(childAtPosition(
                allOf(withId(R.id.list), isEnabled()),
                0),
                isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.phone_number_edit_text), isDisplayed())).perform(click());

        onView(allOf(withId(R.id.phone_number_edit_text), isDisplayed()))
                .perform(replaceText(phoneNumber), closeSoftKeyboard());

        onView(allOf(withId(R.id.btn_apply), withText("apply"), isDisplayed())).perform(click());

        onView(allOf(withId(R.id.phone_number),
                childAtPosition(
                        childAtPosition(
                                withId(R.id.list),
                                0),
                        2),
                isDisplayed()))
                .check(matches(withText(phoneNumber)));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
