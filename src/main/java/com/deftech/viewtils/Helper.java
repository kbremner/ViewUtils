package com.deftech.viewtils;

import android.view.ViewGroup;
import android.app.Activity;


public class Helper {
    protected final Object instance;
    private final Class<?> instanceClass;

    Helper(Object instance, Class<?> instanceClass){
        this.instance = instance;
        this.instanceClass = instanceClass;
    }

    public MethodRunner executeOnUiThread(String methodName){
        return new MethodRunner(methodName, instance, instanceClass);
    }


    public static ActivityHelper with(Activity activity){
        return new ActivityHelper(activity);
    }
    
    public static ViewGroupHelper with(ViewGroup group){
        return new ViewGroupHelper(group);
    }

    public static <T> Helper with(T instance, Class<T> instanceClass){
        return new Helper(instance, instanceClass);
    }
}
