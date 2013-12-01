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

    public T where(Requirement<? super T> requirements){
        return where(group, requirements);
    }

    private T where(ViewGroup group, Requirement<? super T> requirement){
        for(int i=0; i<group.getChildCount(); i++){
            View currentView = group.getChildAt(i);

            // Check that the view is the correct type and meets the requirement
            if(viewClass.isInstance(currentView) &&
                    requirement.matchesRequirement(viewClass.cast(currentView))) {
                return viewClass.cast(currentView);
            }

            // If it was a ViewGroup, look in there as well
            if(currentView instanceof ViewGroup){
                return where((ViewGroup) currentView, requirement);
            }
        }

        return null; // No match found...
    }



    public List<T> allWhere(Requirement<? super T> requirement){
        return allWhere(group, requirement);
    }

    private List<T> allWhere(ViewGroup group, Requirement<? super T> requirement){
        List<T> results = new ArrayList<T>();

        for(int i=0; i<group.getChildCount(); i++){
            View currentView = group.getChildAt(i);

            // Check that the view is the correct type and meets the requirement
            if(viewClass.isInstance(currentView) &&
                    requirement.matchesRequirement(viewClass.cast(currentView))){
                results.add(viewClass.cast(currentView));
            }

            // If it was a ViewGroup, look in there as well
            if(currentView instanceof ViewGroup){
                results.addAll(allWhere((ViewGroup) currentView, requirement));
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
