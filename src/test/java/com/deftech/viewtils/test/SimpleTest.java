package com.deftech.viewtils.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deftech.viewtils.matchers.Requirement;
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
        TextView view = with(activity).find(TextView.class).where().textIs("New Text");
        assertNotNull(view);
        assertEquals(view.getText().toString(), "New Text");
    }

    @Test
    public void testFindTextViewWithId() throws Exception {
        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
        TextView view = with(activity).find(TextView.class).where().idIs(R.id.textView);
        assertNotNull(view);
        assertEquals(view.getId(), R.id.textView);
    }

    @Test
    public void testFindTextViewWithCustomReq() throws Exception {
        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
        TextView view = with(activity).find(TextView.class).where().matches(new Requirement<View>() {
            @Override public boolean match(View instance) {
                return instance.getVisibility() == View.VISIBLE;
            }
        });
        assertNotNull(view);
        assertEquals(view.getVisibility(), View.VISIBLE);
    }

    @Test
    public void testFindButton() throws Exception {
        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
        Button view = with(activity).find(Button.class).where().textIs("New Button");
        assertNotNull(view);
        assertEquals(view.getText().toString(), "New Button");
    }

    @Test
    public void testFindButtonWithId() throws Exception {
        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
        Button view = with(activity).find(Button.class).where().idIs(R.id.button);
        assertNotNull(view);
        assertEquals(view.getId(), R.id.button);
    }

    @Test
    public void testMatcherIs() throws Exception {
        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
        Button b = with(activity).find(Button.class).where().is(activity.findViewById(R.id.button));
        assertNotNull(b);
        assertEquals(b, activity.findViewById(R.id.button));
    }

    @Test
    public void testFindButtonWithCustomReq() throws Exception {
        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
        Button view = with(activity).find(Button.class).where().matches(new Requirement<View>() {
            @Override public boolean match(View instance) {
                return instance.getVisibility() == View.VISIBLE;
            }
        });
        assertNotNull(view);
        assertEquals(view.getVisibility(), View.VISIBLE);
    }

    @Test
    public void testSetText() throws Exception {
        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();

        // Get the view
        TextView view = with(activity).find(TextView.class).where().textIs("New Text");
        assertNotNull(view);
        assertEquals(view.getText().toString(), "New Text");

        // Change the text
        with(view, TextView.class)
                .usingRobolectric()
                .executeOnUiThread("setText")
                .withParameter("Set text", CharSequence.class)
                .returningNothing();


        // Validate that the change happened
        view = with(activity).find(TextView.class).where().textIs("Set text");
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
