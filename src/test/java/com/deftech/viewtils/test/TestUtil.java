package com.deftech.viewtils.test;

import android.app.Activity;

import static org.robolectric.Robolectric.buildActivity;


class TestUtil {
    private TestUtil(){}

    public static <T extends Activity> T createActivity(Class<T> activityClass){
        return buildActivity(activityClass).create().start().resume().visible().get();
    }
}
