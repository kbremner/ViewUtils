package com.deftech.viewtils.helpers;

import android.view.View;
import android.widget.Spinner;
import com.deftech.viewtils.matchers.SpinnerMatcher;


/***
 * Helps interact with a {@link android.view.ViewGroup}. When
 * searching for a view, it starts looking in the provided
 * {@link android.view.ViewGroup} and also looks in any nested {@link android.view.ViewGroup}'s
 * @see com.deftech.viewtils.helpers.ActivityHelper
 */
public class SpinnerHelper extends Helper<Spinner,View> {

    /***
     * Create a new instance to help interact with the
     * provided {@link android.view.ViewGroup}
     * @param instance ViewGroup to be used for carrying out operations
     */
    SpinnerHelper(Spinner instance) {
        super(instance);
    }

    /***
     * Returns a {@link SpinnerMatcher} to help find a view of the provided type
     * within this Helper's ViewGroup
     * @param view the class that represents the type of view to find
     * @param <T> the type of view to find
     * @return a {@link SpinnerMatcher} that can be used to find a view of the specified type
     */
    public <T extends View> SpinnerMatcher<T> find(Class<T> view){
        return new SpinnerMatcher<T>(instance, view, false);
    }

    /***
     * Provides a {@link SpinnerMatcher} to help in click a view of the specified type
     * @param viewClass class that represents the type of view to be clicked
     * @param <T> the type of view to be clicked
     * @return A {@link SpinnerMatcher} to help in clicking a view
     */
    public <T extends View> SpinnerMatcher<T> click(Class<T> viewClass){
        return new SpinnerMatcher<T>(instance, viewClass, true);
    }
}
