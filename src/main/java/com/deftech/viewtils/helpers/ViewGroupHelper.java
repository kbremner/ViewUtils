package com.deftech.viewtils.helpers;

import android.view.View;
import android.view.ViewGroup;
import com.deftech.viewtils.matchers.Matcher;
import com.deftech.viewtils.matchers.ViewMatcher;


/***
 * Helps interact with a {@link ViewGroup}. When
 * searching for a view, it starts looking in the provided
 * {@link ViewGroup} and also looks in any nested {@link ViewGroup}'s
 * @see ActivityHelper
 */
public class ViewGroupHelper extends Helper<ViewGroup,View> {

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
        return new ViewMatcher<T>(instance, view, false);
    }

    /***
     * Provides a {@link ViewMatcher} to help in click a view of the specified type
     * @param viewClass class that represents the type of view to be clicked
     * @param <T> the type of view to be clicked
     * @return A {@link ViewMatcher} to help in clicking a view
     */
    public <T extends View> ViewMatcher<T> click(Class<T> viewClass){
        return new ViewMatcher<T>(instance, viewClass, true);
    }
}
