package com.deftech.viewtils;

import android.view.ViewGroup;
import android.app.Activity;

/***
 * Helper instances provide support for interacting with an object
 * @see ActivityHelper
 * @see ViewGroupHelper
 */
public class Helper {
    protected final Object instance;
    protected final Class<?> instanceClass;

    /***
     * Contructor that provides an instance that
     * is to be used to carry out further operations
     * @param instance The instance to be used by the helper
     * to carry out supported tasks
     */
    protected Helper(Object instance){
        this(instance, instance.getClass());
    }
    
    /***
     * Contructor that provides a class that
     * is to be used to carry out further operations
     * @param instanceClass The class to be used by the helper
     * to carry out supported tasks
     */
    protected Helper(Class<?> instanceClass){
        this(null, instanceClass);
    }
    
    /***
     * Contructor that provides an insstance and a class that
     * are to be used to carry out further operations
     * @param instance The instance to be used by the helper
     * @param instanceClass The class to be used by the helper
     */
    protected Helper(Object instance, Class<?> instanceClass){
        this.instance = instance;
        this.instanceClass = instanceClass;
    }


    /***
     * Returns a {@link MethodRunner} that can be used to
     * call a method with the name provided on the UI thread.
     * @param methodName the name of the method to be executed
     */
    public MethodRunner executeOnUiThread(String methodName){
        return new MethodRunner(methodName, instance, instanceClass);
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
    
    /***
     * Returns a helper that can be used to execute a static
     * method implemented by the provided class on the UI thread
     * @param instanceClass The class that implements the
     * static methods that are to be called
     */
    public static Helper with(Class<?> instanceClass){
        return new Helper(instanceClass);
    }

    /***
     * Returns a basic {@link Helper} instance that can be
     * used to execute methods on the UI thread using the
     * {@link #executeOnUiThread(String)} method
     * @param instance The object that implements the
     * method to be called
     */
    public static Helper with(Object instance){
        return new Helper(instance);
    }
}
