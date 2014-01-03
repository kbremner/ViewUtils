package com.deftech.viewtils.helpers;

import android.view.ViewGroup;
import android.app.Activity;
import com.deftech.viewtils.MethodRunner;
import com.deftech.viewtils.matchers.Matcher;


/***
 * Helper instances provide support for interacting with an object
 * @see ActivityHelper
 * @see ViewGroupHelper
 */
public abstract class Helper<T,C> {
    protected final T instance;
    
    /***
     * Contructor that provides an insstance and a class that
     * are to be used to carry out further operations
     * @param instance The instance to be used by the helper
     */
    protected Helper(T instance){
        this.instance = instance;
    }

    /***
     * Get a {@link Matcher} instance for finding an instance of the specified type
     * @param instanceClass Class defining the type of instance to find
     * @param <T> Type of instance to find
     * @return {@link Matcher} instance for finding an instance of the specified type
     */
    public abstract <T extends C> Matcher<T> find(Class<T> instanceClass);

    /***
     * Get a {@link Matcher} instance for clicking an instance of the specified type
     * @param instanceClass Class defining the type of instance to click
     * @param <T> Type of instance to click
     * @return {@link Matcher} instance for clicking an instance of the specified type
     */
    public abstract <T extends C> Matcher<T> click(Class<T> instanceClass);

    /***
     * Returns a {@link com.deftech.viewtils.MethodRunner} that can be used to
     * call a method with the name provided on the UI thread.
     * @param methodName the name of the method to be executed
     */
    public MethodRunner executeOnUiThread(String methodName){
        return MethodRunner.executeOnUiThread(methodName, instance);
    }

    /***
     * Returns an {@link ActivityHelper} instance to help carry
     * out various tasks using the provided {@link Activity}
     * @param activity {@link Activity} to be used for carrying out
     * supported tasks
     */
    public static ActivityHelper with(Activity activity){
        return new ActivityHelper(activity);
    }
    
    /***
     * Returns a {@link ViewGroupHelper} instance to help carry
     * out various tasks using the provided {@link ViewGroup}
     * @param group {@link ViewGroup} to be used for carrying out
     * supported tasks
     */
    public static ViewGroupHelper with(ViewGroup group){
        return new ViewGroupHelper(group);
    }
}
