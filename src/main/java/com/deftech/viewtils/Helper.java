package com.deftech.viewtils;

import android.view.ViewGroup;
import android.app.Activity;

/***
 * A Helper provides support for interacting
 * with any object, but in particular {@link Activity}'s and
 * {@link ViewGroup}'s.
 */
public class Helper {
    protected final Object instance;

    protected Helper(Object instance){
        this.instance = instance;
    }

    public MethodRunner executeOnUiThread(String methodName){
        return new MethodRunner(methodName, instance);
    }


    public static ActivityHelper with(Activity activity){
        return new ActivityHelper(activity);
    }
    
    public static ViewGroupHelper with(ViewGroup group){
        return new ViewGroupHelper(group);
    }

    public static Helper with(Object instance){
        return new Helper(instance);
    }
}
