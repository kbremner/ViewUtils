package com.deftech.viewtils.matchers;

import android.view.View;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;


public class SpinnerMatcher<T extends View> implements Matcher<T> {
    private final Spinner spinner;
    private final Class<T> viewClass;
    private final boolean clicking;

    public SpinnerMatcher(Spinner group, Class<T> viewClass, boolean clicking){
        this.spinner = group;
        this.viewClass = viewClass;
        this.clicking = clicking;
    }


    public T where(Requirement<? super T> requirement){
        return find(spinner, requirement);
    }

    public List<T> allWhere(Requirement<? super T> requirement){
        T result = where(requirement);
        List<T> results = new ArrayList<T>();
        if(result != null) results.add(result);
        return results;
    }


    private T find(Spinner group, Requirement<? super T> requirement){
        SpinnerAdapter adapter = group.getAdapter();

        for(int i=0; i < adapter.getCount(); i++){
            View currentView = adapter.getView(i, null, group);

            if(viewClass.isInstance(currentView) &&
                    requirement.matchesRequirement(viewClass.cast(currentView))){

                if(clicking) spinner.setSelection(i);
                return viewClass.cast(currentView);
            }
        }

        return null;
    }
}
