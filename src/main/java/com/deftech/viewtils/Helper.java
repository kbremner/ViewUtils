package com.deftech.viewtils;

import android.view.ViewGroup;
import android.app.Activity;


public class Helper<T> {
    private boolean withRobolectric;
    private final T instance;
    private final Class<T> instanceClass;

    Helper(T instance, Class<T> instanceClass){
        this.instance = instance;
        this.instanceClass = instanceClass;
    }

    public T getInstance(){ return instance; }

    public Helper<T> usingRobolectric(){
        withRobolectric = true;
        return this;
    }

    public MethodRunner executeOnUiThread(String methodName){
        return new MethodRunner(methodName, instance, instanceClass, withRobolectric);
    }


    public static ActivityHelper with(Activity activity){
        return new ActivityHelper(activity);
    }
    
    public static ViewGroupHelper with(ViewGroup group){
        return new ViewGroupHelper(group);
    }

    public static <T> Helper<T> with(T instance, Class<T> instanceClass){
        return new Helper<T>(instance, instanceClass);
    }
}
