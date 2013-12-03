package com.deftech.viewtils;

import android.view.ViewGroup;
import android.app.Activity;


public class Helper {
    protected final Object instance;

    <T> Helper(T instance){
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

    public static <T> Helper with(T instance){
        return new Helper(instance);
    }
}
