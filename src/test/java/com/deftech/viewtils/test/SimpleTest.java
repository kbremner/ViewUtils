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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static com.deftech.viewtils.Helper.with;
import static com.deftech.viewtils.matchers.ViewMatcher.*;
import static org.junit.Assert.assertNull;


@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/test/resources/AndroidManifest.xml")
public class SimpleTest {

    @Test
    public void testFindTextView() throws Exception {
        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
        TextView view = with(activity).find(TextView.class).where(textIs("New Text"));
        assertNotNull(view);
        assertEquals(view.getText().toString(), "New Text");
    }

    @Test
    public void testFindTextViewWithId() throws Exception {
        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
        TextView view = with(activity).find(TextView.class).where(idIs(R.id.textView));
        assertNotNull(view);
        assertEquals(view.getId(), R.id.textView);
    }

    @Test
    public void testFindTextViewWithCustomReq() throws Exception {
        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
        TextView view = with(activity).find(TextView.class).where(new Requirement<View>() {
            @Override
            public boolean matchesRequirement(View instance) {
                return instance.getVisibility() == View.VISIBLE;
            }
        });
        assertNotNull(view);
        assertEquals(view.getVisibility(), View.VISIBLE);
    }

    @Test
    public void testFindButton() throws Exception {
        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
        Button view = with(activity).find(Button.class).where(textIs("New Button"));
        assertNotNull(view);
        assertEquals(view.getText().toString(), "New Button");
    }

    @Test
    public void testFindButtonWithId() throws Exception {
        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
        Button view = with(activity).find(Button.class).where(idIs(R.id.button));
        assertNotNull(view);
        assertEquals(view.getId(), R.id.button);
    }

    @Test
    public void testMatcherIs() throws Exception {
        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
        Button b = with(activity).find(Button.class).where(is(activity.findViewById(R.id.button)));
        assertNotNull(b);
        assertEquals(b, activity.findViewById(R.id.button));
    }

    @Test
    public void testFindButtonWithCustomReq() throws Exception {
        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
        Button view = with(activity).find(Button.class).where(new Requirement<View>() {
            @Override
            public boolean matchesRequirement(View instance) {
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

    @Test
    public void testFindAllViews(){
        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
        List<View> results = with(activity).find(View.class).allWhere(any());
        assertEquals(results.size(), 3);  // LinearLayout, EditText, TextView
    }

    @Test
    public void testFindAllTextViews(){
        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
        Set<Requirement<? super TextView>> reqs = new HashSet<Requirement<? super TextView>>();
        reqs.add(any());
        List<TextView> results = with(activity).find(TextView.class).allWhere(any());
        assertEquals(results.size(), 2);  // EditText, TextView
    }

    @Test
    public void testFindTextViewsMultpleMatchers(){
        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
        Set<Requirement<? super TextView>> reqs = new HashSet<Requirement<? super TextView>>();
        reqs.add(textIs("New Text"));
        reqs.add(idIs(R.id.textView));
        TextView result = with(activity).find(TextView.class).where(any());

        assertNotNull(result);
        assertEquals(result.getText().toString(), "New Text");
    }

    @Test
    public void testDontFindTextViewsMultpleMatchers(){
        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
        Set<Requirement<? super TextView>> reqs = new HashSet<Requirement<? super TextView>>();
        reqs.add(textIs("New Text"));
        reqs.add(idIs(R.id.button));
        TextView result = with(activity).find(TextView.class).where(reqs);
        assertNull(result);
    }

    @Test
    public void testFindAllTextViewsWithId(){
        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
        List<TextView> results = with(activity).find(TextView.class).allWhere(idIs(R.id.textView));
        assertEquals(results.size(), 1);  // TextView
    }


    public static final class SimpleActivity extends Activity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main_layout);
        }
    }
}
