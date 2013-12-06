package com.deftech.viewtils.test;


import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import com.deftech.viewtils.matchers.TextViewMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.TimeUnit;

import static com.deftech.viewtils.Helper.with;
import static com.deftech.viewtils.test.TestUtil.createActivity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/test/resources/AndroidManifest.xml")
public class MethodRunnerTest {
    private static boolean methodRan = false;
    
    @Test
    public void testSetText() {
        Activity activity = createActivity();

        // Get the view
        TextView view = with(activity).find(TextView.class).where(TextViewMatcher.textIs(R.string.tv_str));
        assertNotNull(view);
        assertEquals(view.getText().toString(), view.getContext().getString(R.string.tv_str));

        // Change the text
        with(view).executeOnUiThread("setText")
                .withParameter("Set text", CharSequence.class)
                .usingRobolectric()
                .returningNothing();

        // Validate that the change happened
        view = with(activity).find(TextView.class).where(TextViewMatcher.textIs("Set text"));
        assertNotNull(view);
        assertEquals(view.getText().toString(), "Set text");
    }

    @Test
    public void testDelayedSetText() {
        Activity activity = createActivity();

        // Get the view
        TextView view = with(activity).find(TextView.class).where(TextViewMatcher.textIs(R.string.tv_str));
        assertNotNull(view);
        assertEquals(view.getText().toString(), view.getContext().getString(R.string.tv_str));

        // Change the text
        with(view).executeOnUiThread("setText")
                .withParameter("Set text", CharSequence.class)
                .usingRobolectric()
                .in(1000, TimeUnit.MILLISECONDS) // wait a second
                .returningNothing();

        // Validate that the change happened
        view = with(activity).find(TextView.class).where(TextViewMatcher.textIs("Set text"));
        assertNotNull(view);
        assertEquals(view.getText().toString(), "Set text");
    }

    @Test
    public void testSetTextWithDifferentHandler(){
        Activity activity = createActivity();

        // Get the view
        TextView view = with(activity).find(TextView.class).where(TextViewMatcher.textIs(R.string.tv_str));
        assertNotNull(view);
        assertEquals(view.getText().toString(), view.getContext().getString(R.string.tv_str));

        // Change the text
        with(view).executeOnUiThread("setText")
                .withParameter("Set text", CharSequence.class)
                .withHandler(new Handler(Looper.getMainLooper()))
                .usingRobolectric()
                .returningNothing();

        // Validate that the change happened
        view = with(activity).find(TextView.class).where(TextViewMatcher.textIs("Set text"));
        assertNotNull(view);
        assertEquals(view.getText().toString(), "Set text");
    }
    
    @Test
    public void testRunStaticMethod(){
        methodRan = false;
        with(MethodRunnerTest.class).executeOnUiThread("simpleStaticMethod")
            .usingRobolectric()
            .returningNothing();
            
        assertTrue("Static method didn't run", methodRan);
    }
    
    
    public static void simpleStaticMethod(){
        methodRan = true;
    }
}
