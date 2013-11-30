package com.deftech.viewtils.finders;

import android.view.View;
import android.view.ViewGroup;
import com.deftech.viewtils.matchers.ViewMatcher;

public class ViewFinder<T extends View> implements Finder<T> {
    private final Class<T> viewClass;
    private final ViewGroup group;

    public ViewFinder(ViewGroup group, Class<T> viewClass) {
        this.group = group;
        this.viewClass = viewClass;
    }

    @Override
    public ViewMatcher<T> where(){
        return new ViewMatcher<T>(group, viewClass);
    }
}
