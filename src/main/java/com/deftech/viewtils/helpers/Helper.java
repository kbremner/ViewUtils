package com.deftech.viewtils.helpers;

import android.view.ViewGroup;
import android.app.Activity;

public abstract class Helper<T> {
    private final T instance;

    Helper(T instance){
        this.instance = instance;
    }

    public T getInstance(){ return instance; }

    public static ActivityHelper with(Activity activity){
        return new ActivityHelper(activity);
    }
    
    public static ViewGroupHelper with(ViewGroup group){
        return new ViewGroupHelper(group);
    }
}
