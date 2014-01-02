package com.deftech.viewtils.matchers;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

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

        if(group instanceof Spinner){
            SpinnerMatcher<T> matcher = new SpinnerMatcher<T>((Spinner) group, viewClass, clicking);
            results.addAll(matcher.allWhere(requirement));
        } else {
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
