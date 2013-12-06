package com.deftech.viewtils.test;

import static org.robolectric.Robolectric.buildActivity;


class TestUtil {
    private TestUtil(){}

    public static SimpleActivity createActivity(){
        return buildActivity(SimpleActivity.class).create().start().resume().visible().get();
    }
}
