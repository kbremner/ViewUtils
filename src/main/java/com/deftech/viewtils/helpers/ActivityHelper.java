package com.deftech.viewtils.helpers;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import com.deftech.viewtils.finders.ViewFinder;

public class ActivityHelper extends Helper<Activity> {
    ActivityHelper(Activity instance) {
        super(instance);
    }

    public <T extends View> ViewFinder<T> find(Class<T> view){
        return new ViewFinder<T>((ViewGroup)getInstance().findViewById(android.R.id.content), view);
    }
}
