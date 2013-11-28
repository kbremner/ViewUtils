package com.deftech.viewtils;

import android.view.ViewGroup;
import android.app.Activity;


public class Helper<T,C extends Helper> {
    private boolean withRobolectric;
    private final T instance;
    private final Class<T> instanceClass;
    private final Class<C> helperClass;

    Helper(T instance, Class<T> instanceClass, Class<C> helperClass){
        this.instance = instance;
        this.instanceClass = instanceClass;
        this.helperClass = helperClass;
    }

    public T getInstance(){ return instance; }

    public C usingRobolectric(){
        withRobolectric = true;
        return helperClass.cast(this);
    }

    public MethodRunner<T> executeOnUiThread(String methodName){
        return new MethodRunner<T>(methodName, instance, instanceClass, withRobolectric);
    }


    public static ActivityHelper with(Activity activity){
        return new ActivityHelper(activity);
    }
    
    public static ViewGroupHelper with(ViewGroup group){
        return new ViewGroupHelper(group);
    }

    public static <T> Helper<T,Helper> with(T instance, Class<T> instanceClass){
        return new Helper<T,Helper>(instance, instanceClass, Helper.class);
    }
}
