package com.deftech.viewtils.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static com.deftech.viewtils.Helper.with;


@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/test/resources/AndroidManifest.xml")
public class SimpleTest {

    @Test
    public void testFindTextView() throws Exception {
        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
        TextView view = with(activity).find(TextView.class).where("New Text");
        assertNotNull(view);
        assertEquals(view.getText().toString(), "New Text");
    }

    @Test
    public void testFindButton() throws Exception {
        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
        Button view = with(activity).find(Button.class).where("New Button");
        assertNotNull(view);
        assertEquals(view.getText().toString(), "New Button");
    }

    @Test
    public void testSetText() throws Exception {
        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();

        // Get the view
        TextView view = with(activity).find(TextView.class).where("New Text");
        assertNotNull(view);
        assertEquals(view.getText().toString(), "New Text");

        // Change the text
        with(view, TextView.class)
                .usingRobolectric()
                .executeOnUiThread("setText")
                .withParameter("Set text", CharSequence.class)
                .returningNothing();


        // Validate that the change happened
        view = with(activity).find(TextView.class).where("Set text");
        assertNotNull(view);
        assertEquals(view.getText().toString(), "Set text");

    }



    public static final class SimpleActivity extends Activity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main_layout);
        }
    }
}
