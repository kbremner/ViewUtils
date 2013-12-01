package com.deftech.viewtils;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import com.deftech.viewtils.matchers.Requirement;
import com.deftech.viewtils.matchers.ViewMatcher;

public class ActivityHelper extends Helper<Activity> {
    private final ViewGroupHelper groupHelper;

    ActivityHelper(Activity instance) {
        super(instance, Activity.class);
        this.groupHelper = new ViewGroupHelper((ViewGroup)instance.findViewById(android.R.id.content));
    }

    public <T extends View> ViewMatcher<T> find(Class<T> view){
        return groupHelper.find(view);
    }

    public <T extends View> boolean click(Class<T> viewClass, Requirement<? super T> requirement){
        return groupHelper.click(viewClass, requirement);
    }
}
