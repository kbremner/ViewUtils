package com.deftech.viewtils.matchers;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ViewMatcher<T extends View> extends BaseMatcher<T> {
    private final ViewGroup group;
    private final Class<T> viewClass;

    public ViewMatcher(ViewGroup group, Class<T> viewClass){
        this.group = group;
        this.viewClass = viewClass;
    }

    public T where(Requirement<? super T> comparable){
        return where(group, comparable);
    }

    private T where(ViewGroup group, Requirement<? super T> requirement){
        T result = null;
        for(int i=0; i<group.getChildCount(); i++){
            View currentView = group.getChildAt(i);

            if(viewClass.isInstance(currentView) &&
                    requirement.matchesRequirement(viewClass.cast(currentView))) {
                return viewClass.cast(currentView);
            } else if(currentView instanceof ViewGroup){
                return where((ViewGroup) currentView, requirement);
            }
        }
        return result;
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
}
