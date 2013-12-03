package com.deftech.viewtils.test;


import android.app.Activity;
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

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/test/resources/AndroidManifest.xml")
public class MethodRunnerTest {
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
                .in(100, TimeUnit.MILLISECONDS)
                .returningNothing();

        // Validate that the change happened
        view = with(activity).find(TextView.class).where(TextViewMatcher.textIs("Set text"));
        assertNotNull(view);
        assertEquals(view.getText().toString(), "Set text");
    }

}
