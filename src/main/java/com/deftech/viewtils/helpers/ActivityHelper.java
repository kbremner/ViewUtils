package com.deftech.viewtils.helpers;


import android.app.Activity;
import android.view.ViewGroup;

/***
 * Helps interact with an {@link Activity}. When
 * searching for a view, it starts at the view returned
 * by {@code activity.findViewById(android.R.id.content)}
 * @see ViewGroupHelper
 */
public class ActivityHelper extends ViewGroupHelper {
    private final Activity activity;

    /***
     * Create a new instance to help interact with the
     * provided {@link Activity}
     * @param instance Activity to be used for carrying out operations
     */
    ActivityHelper(Activity instance) {
        super((ViewGroup)instance.findViewById(android.R.id.content));
        this.activity = instance;
    }
}
