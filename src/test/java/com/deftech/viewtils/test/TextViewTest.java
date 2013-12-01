package com.deftech.viewtils.test;

import android.view.View;
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
import static com.deftech.viewtils.matchers.BaseMatcher.exists;
import static com.deftech.viewtils.matchers.ViewMatcher.idIs;
import static com.deftech.viewtils.matchers.ViewMatcher.textIs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/test/resources/AndroidManifest.xml")
public class TextViewTest {

    @Test
    public void testFindTextView() throws Exception {
        TextView view = with(createActivity()).find(TextView.class).where(textIs("New Text"));
        assertNotNull(view);
        assertEquals(view.getText().toString(), "New Text");
    }

    @Test
    public void testFindTextViewWithId() throws Exception {
        TextView view = with(createActivity()).find(TextView.class).where(idIs(R.id.textView));
        assertNotNull(view);
        assertEquals(view.getId(), R.id.textView);
    }

    @Test
    public void testDontFindTextViewWithId() throws Exception {
        assertNull(with(createActivity()).find(TextView.class).where(idIs(0)));
    }

    @Test
    public void testFindTextViewWithCustomReq() throws Exception {
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
        List<TextView> result = with(createActivity()).find(TextView.class).allWhere(reqs);

        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getText().toString(), "New Text");
    }

    @Test
    public void testDontFindTextViewsMultpleMatchers(){
        Set<Requirement<? super TextView>> reqs = new HashSet<Requirement<? super TextView>>();
        reqs.add(textIs("New Text"));
        reqs.add(idIs(R.id.button));
        TextView result = with(createActivity()).find(TextView.class).where(reqs);
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
}
