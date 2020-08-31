package com.inventrax_pepsi;

import android.test.ActivityInstrumentationTestCase2;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.inventrax.SplashScreenActivityTest \
 * com.inventrax.tests/android.test.InstrumentationTestRunner
 */
public class SplashScreenActivityTest extends ActivityInstrumentationTestCase2<SplashScreenActivity> {

    public SplashScreenActivityTest() {
        super("com.inventrax", SplashScreenActivity.class);
    }

}
