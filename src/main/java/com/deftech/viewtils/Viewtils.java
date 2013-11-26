package com.deftech.viewtils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;

/***
 * First attempt at looking up views based on content
 */
public class Viewtils {
    private Viewtils(){}

    public static <T extends View> T findViewWithContent(Activity activity, String content, Class<T> viewClass){
        View contentView = activity.findViewById(android.R.id.content);
        if(contentView == null || !(contentView instanceof ViewGroup)) return null;
        return findViewWithContent((ViewGroup)contentView, content, viewClass);
    }

    public static <T extends View> T findViewWithContent(ViewGroup group, String content, Class<T> viewClass){
        T result = null;
        for(int i=0; i<group.getChildCount(); i++){
            View currentView = group.getChildAt(i);

            if(currentView instanceof ViewGroup){
                /* It's a viewgroup, so search in it */
                result = findViewWithContent((ViewGroup)currentView, content, viewClass);
            } else {
                /* Not a viewgroup, check to see if the class has a getText() method */
                try {
                    Method method = currentView.getClass().getMethod("getText");
                    CharSequence seq = (CharSequence) method.invoke(currentView);
                    if((content == null && seq == null) || (seq != null && seq.toString().equals(content))
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
