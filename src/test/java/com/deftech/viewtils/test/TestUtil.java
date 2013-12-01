package com.deftech.viewtils.test;

import android.app.Activity;

import static org.robolectric.Robolectric.buildActivity;

class TestUtil {
    private TestUtil(){}

    public static Activity createActivity(){
        return buildActivity(SimpleActivity.class).create().get();
    }
}
