package com.deftech.viewtils.test;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.deftech.viewtils.matchers.Requirement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.deftech.viewtils.test.TestUtil.createActivity;
import static com.deftech.viewtils.Helper.with;
import static com.deftech.viewtils.matchers.TextViewMatcher.*;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/test/resources/AndroidManifest.xml")
public class ViewMatcherTest {

    @Test
    public void testFindTextView() {
        TextView view = with(createActivity()).find(TextView.class).where(textIs("New Text"));
        assertNotNull(view);
        assertEquals(view.getText().toString(), "New Text");
    }

    @Test
    public void testFindTextViewWithId() {
        TextView view = with(createActivity()).find(TextView.class).where(idIs(R.id.textView));
        assertNotNull(view);
        assertEquals(view.getId(), R.id.textView);
    }

    @Test
    public void testDontFindTextViewWithId() {
        assertNull(with(createActivity()).find(TextView.class).where(idIs(0)));
    }

    @Test
    public void testFindTextViewWithCustomReq() {
        TextView view = with(createActivity()).find(TextView.class).where(new Requirement<View>() {
            @Override
            public boolean matchesRequirement(View instance) {
                return instance.getVisibility() == View.VISIBLE;
            }
        });
        assertNotNull(view);
        assertEquals(view.getVisibility(), View.VISIBLE);
    }

    @Test
    public void testFindTextViewsMultpleMatchers(){
        Set<Requirement<? super TextView>> reqs = new HashSet<Requirement<? super TextView>>();
        reqs.add(textIs("New Text"));
        reqs.add(idIs(R.id.textView));
        List<TextView> result = with(createActivity()).find(TextView.class).allWhere(matchesAll(reqs));

        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getText().toString(), "New Text");
    }

    @Test
    public void testDontFindTextViewsMultpleMatchers(){
        Set<Requirement<? super TextView>> reqs = new HashSet<Requirement<? super TextView>>();
        reqs.add(textIs("New Text"));
        reqs.add(idIs(R.id.button));
        TextView result = with(createActivity()).find(TextView.class).where(matchesAll(reqs));
        assertNull(result);
    }

    @Test
    public void testFindAllTextViewsWithId(){
        List<TextView> results = with(createActivity()).find(TextView.class).allWhere(idIs(R.id.textView));
        assertEquals(results.size(), 1);  // TextView
    }

    @Test
    public void testFindAllTextViews(){
        List<TextView> results = with(createActivity()).find(TextView.class).allWhere(exists());
        assertEquals(results.size(), 2);  // Button, TextView
    }



    /* -------- Button tests --------- */
    @Test
    public void testFindButton() {
        Button view = with(createActivity()).find(Button.class).where(textIs("New Button"));
        assertNotNull(view);
        assertEquals(view.getText().toString(), "New Button");
    }

    @Test
    public void testFindButtonWithId() {
        Button view = with(createActivity()).find(Button.class).where(idIs(R.id.button));
        assertNotNull(view);
        assertEquals(view.getId(), R.id.button);
    }

    @Test
    public void testFindButtonWithCustomReq() {
        Button view = with(createActivity()).find(Button.class).where(new Requirement<View>() {
            @Override public boolean matchesRequirement(View instance) {
                return instance.getVisibility() == View.VISIBLE;
            }
        });
        assertNotNull(view);
        assertEquals(view.getVisibility(), View.VISIBLE);
    }

    @Test
    public void testFindButtonMatchesRegex() {
        Button view = with(createActivity()).find(Button.class).where(textMatches(".*Button"));
        assertNotNull(view);
        assertEquals(view.getText().toString(), view.getContext().getString(R.string.btn_str));
    }

    @Test
    public void testFindAllButtons(){
        List<Button> results = with(createActivity()).find(Button.class).allWhere(exists());
        assertEquals(results.size(), 1);  // Button
    }

    @Test
    public void testFindButtonWithStringId() {
        Button view = with(createActivity()).find(Button.class).where(textIs(R.string.btn_str));
        assertNotNull(view);
        assertEquals(view.getText().toString(), view.getContext().getString(R.string.btn_str));
    }

    @Test
    public void testDontFindTextViewWithStringId() {
        assertNull(with(createActivity()).find(Button.class).where(textIs(R.string.tv_str)));
    }

    @Test
    public void testClickButtonWithContent() {
        assertTrue(with(createActivity()).click(Button.class, textIs(R.string.btn_str)));
    }

    @Test
    public void testDontClickButtonWithContent() {
        assertFalse(with(createActivity()).click(Button.class, not(textIs(R.string.btn_str))));
    }



    /* -------- ViewGroup tests --------- */
    @Test
    public void testFindViewGroupWithId(){
        Activity activity = createActivity();
        ViewGroup group = with(activity).find(ViewGroup.class).where(idIs(R.id.layout));
        assertNotNull(group);
        assertEquals(group, activity.findViewById(R.id.layout));
    }
}
