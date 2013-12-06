package com.deftech.viewtils;


import android.view.View;
import com.deftech.viewtils.matchers.Requirement;

import java.util.List;

/***
 * Uses a {@link ViewGroupHelper} to find a class
 * of the specified type and try and click it using
 * {@link android.view.View#performClick()}. If that
 * returns false, the instance will try the next class
 * that meets the specified {@link Requirement}
 * @param <T> Type of view to click
 */
public class ViewClicker<T extends View> {
    private final ViewGroupHelper viewGroupHelper;
    private final Class<T> viewClass;

    /***
     * Creates an instance to help in clicking a view
     * of the specified type
     * @param viewGroupHelper Helper used to find a view of the specified type
     * @param viewClass type of view to find
     */
    public ViewClicker(ViewGroupHelper viewGroupHelper, Class<T> viewClass) {
        this.viewGroupHelper = viewGroupHelper;
        this.viewClass = viewClass;
    }

    /***
     * Finds and clicks the first view of the previously specified type that
     * meets the provided requirement. Note that if {@link android.view.View#performClick()}
     * returns false (i.e. it didn't have an {@link android.view.View.OnClickListener}),
     * then that view will be disregarded
     * @param req Requirement that the view must meet before clicking it
     * @return The view that was clicked, or null if no view met the specified
     * requirement and had an {@link android.view.View.OnClickListener}
     */
    public T where(Requirement<? super T> req){
        // Get all the views that meet the requirement
        List<T> views = viewGroupHelper.find(viewClass).allWhere(req);

        // try and click each match
        for(T v : views){
            if(v.performClick()) {
                return v; // Successfully clicked, return it
            }
        }

        // No view clicked
        return null;
    }
}
