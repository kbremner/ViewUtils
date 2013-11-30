package com.deftech.viewtils.matchers;


import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;

public class ViewMatcher<T extends View> extends BaseMatcher<T> {
    private final ViewGroup group;
    private final Class<T> viewClass;

    public ViewMatcher(ViewGroup group, Class<T> viewClass){
        this.group = group;
        this.viewClass = viewClass;
    }

    public T matches(Requirement<? super T> comparable){
        return matches(group, comparable);
    }

    private T matches(ViewGroup group, Requirement<? super T> requirement){
        T result = null;
        for(int i=0; i<group.getChildCount(); i++){
            View currentView = group.getChildAt(i);

            if(viewClass.isInstance(currentView) &&
                    requirement.match(viewClass.cast(currentView))) {
                return viewClass.cast(currentView);
            } else if(currentView instanceof ViewGroup){
                return matches((ViewGroup)currentView, requirement);
            }
        }
        return result;
    }

    public T idIs(final int id){
        return matches(new Requirement<T>(){
            @Override public boolean match(T t){
               return (t.getId() == id);
            }
        });
    }

    public T textIs(final String content){
        return matches(new Requirement<T>(){
            @Override
            public boolean match(T t) {
                try {
                    Method method = t.getClass().getMethod("getText");
                    Object seq = method.invoke(t);
                    if(CharSequence.class.isInstance(seq)){
                        if(((content == null && seq == null) ||
                                (seq != null && seq.toString().equals(content)))){
                            return true;
                        }
                    }
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace(); /* Problem calling method */
                }

                return false;  //Doesn't match
            }
        });
    }
}
