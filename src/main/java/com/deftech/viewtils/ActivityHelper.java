package com.deftech.viewtils;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import com.deftech.viewtils.matchers.ViewMatcher;

public class ActivityHelper extends Helper<Activity> {
    ActivityHelper(Activity instance) {
        super(instance, Activity.class);
    }

    public <T extends View> ViewMatcher<T> find(Class<T> view){
        return new ViewMatcher<T>((ViewGroup)instance.findViewById(android.R.id.content), view);
    }
}
