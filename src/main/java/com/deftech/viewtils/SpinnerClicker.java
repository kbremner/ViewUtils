package com.deftech.viewtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import com.deftech.viewtils.matchers.Requirement;


public class SpinnerClicker<T extends View> {
    private final Spinner spinner;
    private final Class<T> viewClass;

    public SpinnerClicker(Spinner group, Class<T> viewClass){
        this.spinner = group;
        this.viewClass = viewClass;
    }


    public T where(Requirement<? super T> requirement){
        return find(spinner, requirement);
    }


    private T find(ViewGroup group, Requirement<? super T> requirement){
        int childCount = getChildCount(group);

        for(int i=0; i < childCount; i++){
            View currentView = getChildView(group, i);
            // Check that the view is the correct type and meets the requirement
            if(group instanceof Spinner && viewClass.isInstance(currentView) &&
                    requirement.matchesRequirement(viewClass.cast(currentView))){
                Spinner spinner = (Spinner) group;
                spinner.setSelection(i);
                return viewClass.cast(currentView);
            }

            if(currentView instanceof ViewGroup){
                T result = find(group, requirement);
                if(result != null) return result;
            }
        }

        return null;
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
