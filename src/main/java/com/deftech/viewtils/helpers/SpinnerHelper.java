package com.deftech.viewtils.helpers;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import com.deftech.viewtils.matchers.SpinnerClicker;
import com.deftech.viewtils.matchers.ViewMatcher;

public class SpinnerHelper extends Helper {

    SpinnerHelper(Spinner instance) {
        super(instance);
    }

    /***
     * Returns a {@link com.deftech.viewtils.matchers.ViewMatcher} to help find a view of the provided type
     * within this Helper's ViewGroup
     * @param view the class that represents the type of view to find
     * @param <T> the type of view to find
     * @return a {@link com.deftech.viewtils.matchers.ViewMatcher} that can be used to find a view of the specified type
     */
    public <T extends View> ViewMatcher<T> find(Class<T> view){
        return new ViewMatcher<T>((ViewGroup) instance, view);
    }

    /***
     * Provides a {@link SpinnerClicker} to help in click a view of the specified type
     * @param viewClass class that represents the type of view to be clicked
     * @param <T> the type of view to be clicked
     * @return A ViewClicker to help in clicking a view
     */
    public <T extends View> SpinnerClicker<T> click(Class<T> viewClass){
        return new SpinnerClicker<T>((Spinner)instance, viewClass);
    }
}
