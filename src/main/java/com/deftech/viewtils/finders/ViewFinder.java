package com.deftech.viewtils.finders;

import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;

public class ViewFinder<T extends View> extends Finder<ViewGroup> {
    private final Class<T> viewClass;

    public ViewFinder(ViewGroup group, Class<T> viewClass) {
        super(group);
        this.viewClass = viewClass;
    }

    public T where(String contents){
        T result = null;
        for(int i=0; i<getInstance().getChildCount(); i++){
            View currentView = getInstance().getChildAt(i);

            if(currentView instanceof ViewGroup){
                /* It's a viewgroup, so search in it */
                result = new ViewFinder<T>((ViewGroup)currentView, viewClass).where(contents);
            } else {
                /* Not a viewgroup, check to see if the class has a getText() method */
                try {
                    Method method = currentView.getClass().getMethod("getText");
                    CharSequence seq = (CharSequence) method.invoke(currentView);
                    if((contents == null && seq == null) || (seq != null && seq.toString().equals(contents))
                            && viewClass.isInstance(currentView)){
                        result = viewClass.cast(currentView);
                    }
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace(); /* Problem calling method */
                } catch (ClassCastException e) {
                    e.printStackTrace(); /* Method doesn't return a CharSequence */
                }
            }
        }

        return result;
    }
}
