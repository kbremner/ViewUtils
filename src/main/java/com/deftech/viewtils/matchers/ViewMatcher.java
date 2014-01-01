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
        int childCount = getChildCount(group);

        for(int i=0; i < childCount; i++){
            View currentView = getChildView(group, i);
            // Check that the view is the correct type and meets the requirement
            if(viewClass.isInstance(currentView) &&
                    requirement.matchesRequirement(viewClass.cast(currentView)) &&
                    (!clicking || currentView.performClick())){
                results.add(viewClass.cast(currentView));
            }

            if(currentView instanceof Spinner){
                SpinnerMatcher<T> matcher = new SpinnerMatcher<T>((Spinner) currentView, viewClass, clicking);
                results.addAll(matcher.allWhere(requirement));
            } else if(currentView instanceof ViewGroup){
                results.addAll(find((ViewGroup) currentView, requirement, findFirst));
            }

            if(results.size() > 0 && findFirst) break;
        }

        return results;
    }


    private int getChildCount(ViewGroup group){
        if(group instanceof Spinner) return ((Spinner) group).getAdapter().getCount();
        else return group.getChildCount();
    }

    private View getChildView(ViewGroup group, int position){
        if(group instanceof Spinner){
            return ((Spinner) group).getAdapter().getView(position, null, group);
        } else {
            return group.getChildAt(position);
        }
    }


    public static Requirement<View> idIs(final int id){
        return new Requirement<View>(){
            @Override public boolean matchesRequirement(View t){
               return (t.getId() == id);
            }
        };
    }
}
