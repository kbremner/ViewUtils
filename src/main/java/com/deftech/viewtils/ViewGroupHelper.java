package com.deftech.viewtils;

import android.view.View;
import android.view.ViewGroup;
import com.deftech.viewtils.matchers.Requirement;
import com.deftech.viewtils.matchers.ViewMatcher;

public class ViewGroupHelper extends Helper<ViewGroup> {

    ViewGroupHelper(ViewGroup instance) {
        super(instance, ViewGroup.class);
    }

    public <T extends View> ViewMatcher<T> find(Class<T> view){
        return new ViewMatcher<T>(instance, view);
    }

    public <T extends View> boolean click(Class<T> viewClass, Requirement<? super T> requirement){
        T view = find(viewClass).where(requirement);
        if(view != null){
            view.performClick();
            return true;
        }
        return false;
    }
}
