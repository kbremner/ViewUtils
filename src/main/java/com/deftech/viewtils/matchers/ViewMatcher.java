package com.deftech.viewtils.matchers;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;


public class ViewMatcher<T extends View> extends BaseMatcher<T> {
    private final ViewGroup group;
    private final Class<T> viewClass;
    private final boolean clicking;

    public ViewMatcher(ViewGroup group, Class<T> viewClass, boolean clicking){
        this.group = group;
        this.viewClass = viewClass;
        this.clicking = clicking;
    }

    public T where(Requirement<? super T> requirement){
        List<T> results = find(group, requirement, true);
        return (results.size() > 0) ? results.get(0) : null;
    }

    public List<T> allWhere(Requirement<? super T> requirement){
        return find(group, requirement, false);
    }


    private List<T> find(ViewGroup group, Requirement<? super T> requirement, boolean findFirst){
        List<T> results = new ArrayList<T>();

        // Need to deal with Spinner's differently
        if(group instanceof Spinner){
            return find((Spinner) group, requirement, findFirst);
        }

        // Loop through all the children
        for(int i=0; i < group.getChildCount(); i++){
            View currentView = group.getChildAt(i);
            // Check that the view is the correct type and meets the requirement
            if(viewClass.isInstance(currentView) &&
                    requirement.matchesRequirement(viewClass.cast(currentView)) &&
                    (!clicking || currentView.performClick())){
                results.add(viewClass.cast(currentView));
            }

            // If we're not finding first match or haven't found any matches, and it's a
            // view group, search in the current view
            if((!findFirst || results.size() == 0) && currentView instanceof ViewGroup){
                results.addAll(find((ViewGroup) currentView, requirement, findFirst));
            }

            if(results.size() > 0 && findFirst) break;
        }

        return results;
    }

    private List<T> find(Spinner spinner, Requirement<? super T> requirement, boolean findFirst){
        List<T> results = new ArrayList<T>();
        
        // Spinner only ever has one child... Need to use it's
        // adapter to recreate the child views for inspection
        SpinnerAdapter adapter = spinner.getAdapter();
        if(adapter != null){
            for(int i=0; i < adapter.getCount(); i++){
                View currentView = adapter.getView(i, null, group);
    
                if(viewClass.isInstance(currentView) &&
                        requirement.matchesRequirement(viewClass.cast(currentView))){
    
                    if(clicking) spinner.setSelection(i);
                    results.add(viewClass.cast(currentView));
                    if(results.size() > 0 && findFirst) break;
                }
            }
        }

        return results;
    }


    public static Requirement<View> idIs(final int id){
        return new Requirement<View>(){
            @Override public boolean matchesRequirement(View t){
               return (t.getId() == id);
            }
        };
    }
}
