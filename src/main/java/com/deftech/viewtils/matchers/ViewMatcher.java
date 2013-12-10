package com.deftech.viewtils.matchers;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class ViewMatcher<T extends View> extends BaseMatcher<T> {
    private final ViewGroup group;
    private final Class<T> viewClass;

    public ViewMatcher(ViewGroup group, Class<T> viewClass){
        this.group = group;
        this.viewClass = viewClass;
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

        for(int i=0; i<group.getChildCount(); i++){
            View currentView = group.getChildAt(i);

            // Check that the view is the correct type and meets the requirement
            if(viewClass.isInstance(currentView) &&
                    requirement.matchesRequirement(viewClass.cast(currentView))){
                results.add(viewClass.cast(currentView));
                if(findFirst) break;
            }

            // If it was a ViewGroup, look in there as well
            if(currentView instanceof ViewGroup){
                results.addAll(find((ViewGroup) currentView, requirement, findFirst));
                if(results.size() > 0) break;
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
