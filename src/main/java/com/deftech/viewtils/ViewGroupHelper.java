package com.deftech.viewtils;

import android.view.View;
import android.view.ViewGroup;
import com.deftech.viewtils.finders.ViewFinder;

public class ViewGroupHelper extends Helper<ViewGroup> {

    ViewGroupHelper(ViewGroup instance) {
        super(instance, ViewGroup.class);
    }

    public <T extends View> ViewFinder<T> find(Class<T> view){
        return new ViewFinder<T>(getInstance(), view);
    }

    @Override
    public ViewGroupHelper usingRobolectric(){
        super.usingRobolectric();
        return this;
    }
}
