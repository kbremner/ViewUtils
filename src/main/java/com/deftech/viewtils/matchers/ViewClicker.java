package com.deftech.viewtils.matchers;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/***
 * finds classes of the specified type and tries to click them by
 * calling {@link android.view.View#performClick()}
 * @param <T> Type of view to click
 * @see android.view.View#performClick()
 */
public class ViewClicker<T extends View> extends ViewMatcher<T> {
    
    public ViewClicker(ViewGroup group, Class<T> viewClass) {
        super(group, viewClass);
    }

    /***
     * Finds the first instance of the specified type that
     * met the provided requirement and returned {@code true}
     * when {@link android.view.View#performClick()}  was called
     * @param req Requirement that the instance must meet
     * @return The first instance that met the requirement and was clicked
     */
    @Override
    public T where(final Requirement<? super T> req) {
        return super.where(makeClickReq(req));
    }

    /***
     * Finds all instances of the specified type that
     * met the provided requirement and returned {@code true}
     * when {@link android.view.View#performClick()}  was called
     * @param req Requirement that the instances must meet
     * @return The instances that met the requirement and were clicked
     */
    @Override
    public List<T> allWhere(final Requirement<? super T> req) {
        return super.allWhere(makeClickReq(req));
    }


    private Requirement<T> makeClickReq(final Requirement<? super T> req){
        return new Requirement<T>() {
            @Override public boolean matchesRequirement(T instance) {
                return req.matchesRequirement(instance) && instance.performClick();
            }
        };
    }
}
