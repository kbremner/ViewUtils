package com.deftech.viewtils.matchers;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

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

    @Override
    public T where(Requirement<? super T> requirement){
        List<T> results = find(group, requirement, true);
        return (results.size() > 0) ? results.get(0) : null;
    }

    @Override
    public List<T> allWhere(Requirement<? super T> requirement){
        return find(group, requirement, false);
    }


    private List<T> find(ViewGroup group, Requirement<? super T> requirement, boolean findFirst){
        List<T> results;

        if(group instanceof Spinner){
            // Need to deal with Spinner's differently
            return find((Spinner) group, requirement, findFirst);
        } else if (findFirst) {
            // Only looking for one item, so set size of array list accordingly
            results = new ArrayList<T>(1);
        } else {
            results = new ArrayList<T>();
        }

        // Loop through all the children
        for(int i=0; i < group.getChildCount(); i++){
            View currentView = group.getChildAt(i);
            // Check that the view is the correct type and meets the requirement
            if(viewClass.isInstance(currentView) &&
                    requirement.isMatch(viewClass.cast(currentView)) &&
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
        List<T> results;
        if (findFirst) {
            // Only looking for one item, so set size of array list accordingly
            results = new ArrayList<T>(1);
        } else {
            results = new ArrayList<T>();
        }
        
        // Spinner only ever has one child... Need to use it's
        // adapter to recreate the child views for inspection
        SpinnerAdapter adapter = spinner.getAdapter();
        if(adapter != null){
            for(int i=0; i < adapter.getCount(); i++){
                View currentView = adapter.getView(i, null, group);
    
                if(viewClass.isInstance(currentView) &&
                        requirement.isMatch(viewClass.cast(currentView))){
    
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
            @Override public boolean isMatch(View t){
               return (t.getId() == id);
            }
        };
    }

    public static Requirement<TextView> textIs(final Object content){
        return new Requirement<TextView>() {
            @Override public boolean isMatch(TextView t) {
                return t.getText().toString().equals(content.toString());
            }
        };
    }

    public static Requirement<TextView> textMatches(final String regex){
        return new Requirement<TextView>(){
            @Override public boolean isMatch(TextView t){
                return t.getText().toString().matches(regex);
            }
        };
    }

    public static Requirement<TextView> textIs(final int id){
        return new Requirement<TextView>() {
            @Override public boolean isMatch(TextView t) {
                return t.getText().toString().equals(t.getContext().getString(id));
            }
        };
    }
}
