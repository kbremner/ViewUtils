package com.deftech.viewtils.test;


import android.app.Activity;
import android.widget.TextView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.deftech.viewtils.Helper.with;
import static com.deftech.viewtils.matchers.ViewMatcher.textIs;
import static com.deftech.viewtils.test.TestUtil.createActivity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/test/resources/AndroidManifest.xml")
public class MethodRunnerTest {
    @Test
    public void testSetText() throws Exception {
        Activity activity = createActivity();

        // Get the view
        TextView view = with(activity).find(TextView.class).where(textIs("New Text"));
        assertNotNull(view);
        assertEquals(view.getText().toString(), "New Text");

        // Change the text
        with(view, TextView.class)
                .executeOnUiThread("setText")
                .withParameter("Set text", CharSequence.class)
                .usingRobolectric()
                .returningNothing();

        // Validate that the change happened
        view = with(activity).find(TextView.class).where(textIs("Set text"));
        assertNotNull(view);
        assertEquals(view.getText().toString(), "Set text");
    }
}
