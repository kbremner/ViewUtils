package com.deftech.viewtils;


import android.app.Activity;
import android.view.ViewGroup;

public class ActivityHelper extends ViewGroupHelper {
    private final Activity activity;

    ActivityHelper(Activity instance) {
        super((ViewGroup)instance.findViewById(android.R.id.content));
        this.activity = instance;
    }
}
