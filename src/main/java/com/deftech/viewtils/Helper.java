package com.deftech.viewtils;

import android.view.ViewGroup;
import android.app.Activity;


public abstract class Helper<T,C extends Helper> {
    private boolean withRobolectric;
    private final T instance;
    private final Class<T> instanceClass;

    Helper(T instance, Class<T> instanceClass){
        this.instance = instance;
        this.instanceClass = instanceClass;
    }

    public T getInstance(){ return instance; }

    public Helper<T,C> usingRobolectric(){
        withRobolectric = true;
        return this;
    }


    public static ActivityHelper with(Activity activity){
        return new ActivityHelper(activity);
    }
    
    public static ViewGroupHelper with(ViewGroup group){
        return new ViewGroupHelper(group);
    }

    public static <T> Helper<T,Helper> with(T instance, Class<T> instanceClass){
        return new GenericHelper<T>(instance, instanceClass);
    }


    public MethodRunner<T> executeOnUiThread(String methodName){
        return new MethodRunner<T>(methodName, instance, instanceClass, withRobolectric);
    }
}
