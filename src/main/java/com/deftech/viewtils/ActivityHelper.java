package com.deftech.viewtils;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import com.deftech.viewtils.finders.ViewFinder;

public class ActivityHelper extends Helper<Activity,ActivityHelper> {
    ActivityHelper(Activity instance) {
        super(instance, Activity.class);
    }

    public <T extends View> ViewFinder<T> find(Class<T> view){
        return new ViewFinder<T>((ViewGroup)getInstance().findViewById(android.R.id.content), view);
    }


    @Override
    public ActivityHelper usingRobolectric(){
        super.usingRobolectric();
        return this;
    }
}
