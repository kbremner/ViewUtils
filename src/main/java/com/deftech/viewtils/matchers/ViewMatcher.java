package com.deftech.viewtils.matchers;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class ViewMatcher<T extends View> extends BaseMatcher<T> {
    private final ViewGroup group;
    private final Class<T> viewClass;

    public ViewMatcher(ViewGroup group, Class<T> viewClass){
        this.group = group;
        this.viewClass = viewClass;
    }

    public T where(Set<Requirement<? super T>> requirements){
        return where(group, requirements);
    }

    private T where(ViewGroup group, Set<Requirement<? super T>> requirements){
        T result = null;

        if(requirements == null){
            throw new NullPointerException("Requirements set cannot be null");
        }

        for(int i=0; i<group.getChildCount(); i++){
            View currentView = group.getChildAt(i);

            // Check that the view is the correct type and meets the requirement
            if(viewClass.isInstance(currentView)) {
                // Check that all the requirements were met
                boolean passed = true;
                for(Requirement<? super T> req : requirements){
                    passed = req.matchesRequirement(viewClass.cast(currentView));
                    if(!passed) break;
                }

                // If all the requirements were met (or there
                // were no requirements), return the view
                if(passed) return viewClass.cast(currentView);
            }

            // If it was a ViewGroup, look in there as well
            if(currentView instanceof ViewGroup){
                return where((ViewGroup) currentView, requirements);
            }
        }
        return result;
    }



    public List<T> allWhere(Set<Requirement<? super T>> requirements){
        return allWhere(group, requirements);
    }

    private List<T> allWhere(ViewGroup group, Set<Requirement<? super T>> requirements){
        List<T> results = new ArrayList<T>();

        if(requirements == null){
            throw new NullPointerException("Requirements set cannot be null");
        }

        for(int i=0; i<group.getChildCount(); i++){
            View currentView = group.getChildAt(i);

            // Check that the view is the correct type and meets the requirement
            if(viewClass.isInstance(currentView)) {
                // Check that all the requirements were met
                boolean passed = true;
                for(Requirement<? super T> req : requirements){
                    passed = req.matchesRequirement(viewClass.cast(currentView));
                    if(!passed) break;
                }

                // If all the requirements were met (or there
                // were no requirements), return the view
                if(passed) results.add(viewClass.cast(currentView));
            }

            // If it was a ViewGroup, look in there as well
            if(currentView instanceof ViewGroup){
                results.addAll(allWhere((ViewGroup) currentView, requirements));
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

    public static Requirement<TextView> textIs(final String content){
        return new Requirement<TextView>() {
            @Override public boolean matchesRequirement(TextView t) {
                return (content == null) ? (t.getText() == null) : content.equals(t.getText().toString());
            }
        };
    }

    public static Requirement<TextView> textIs(final int id){
        return new Requirement<TextView>() {
            @Override public boolean matchesRequirement(TextView t) {
                    String content = t.getContext().getString(id);
                    return (content != null && t.getText() != null) && content.equals(t.getText().toString());
            }
        };
    }
}
