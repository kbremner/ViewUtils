package com.deftech.viewtils;

import android.view.View;
import android.view.ViewGroup;
import com.deftech.viewtils.matchers.Requirement;
import com.deftech.viewtils.matchers.ViewMatcher;

/***
 * Helps interact with a {@link ViewGroup}. When
 * searching for a view, it starts looking in the provided
 * {@link ViewGroup} and also looks in any nested {@link ViewGroup}'s
 * @see ActivityHelper
 */
public class ViewGroupHelper extends Helper {

    /***
     * Create a new instance to help interact with the
     * provided {@link ViewGroup}
     * @param instance ViewGroup to be used for carrying out operations
     */
    ViewGroupHelper(ViewGroup instance) {
        super(instance);
    }

    /***
     * Returns a {@link ViewMatcher} to help find a view of the provided type
     * within this Helper's ViewGroup
     * @param view the class that represents the type of view to find
     * @param <T> the type of view to find
     * @return a {@link ViewMatcher} that can be used to find a view of the specified type
     */
    public <T extends View> ViewMatcher<T> find(Class<T> view){
        return new ViewMatcher<T>((ViewGroup) instance, view);
    }

    /***
     * Attempts to click on a view that meets the provided {@link Requirement}
     * @param viewClass class that represents the type of view to be clicked
     * @param requirement a requirement that a view must meet before we click it
     * @param <T> the type of view to be clicked
     * @return true if {@code performClick()} for a view that met the requirement returned true, else false
     */
    public <T extends View> boolean click(Class<T> viewClass, Requirement<? super T> requirement){
        T view = find(viewClass).where(requirement);
        if(view != null){
            return view.performClick();
        }
        return false;
    }
}
