package com.deftech.viewtils.test;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static com.deftech.viewtils.helpers.Helper.with;
import static com.deftech.viewtils.matchers.BaseMatcher.*;
import static com.deftech.viewtils.test.TestUtil.createActivity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/test/resources/AndroidManifest.xml")
public class BaseMatcherTest {
    @Test
    public void testIs() {
        Activity activity = createActivity(SimpleActivity.class);
        Button b = with(activity).find(Button.class).where(is(activity.findViewById(R.id.button)));
        assertNotNull(b);
        assertEquals(activity.findViewById(R.id.button), b);
    }

    @Test
    public void testFindAllViews(){
        List<View> results = with(createActivity(SimpleActivity.class)).find(View.class).allWhere(exists());
        assertEquals(3, results.size());  // LinearLayout, Button, TextView
    }

    @Test
    public void testNot(){
        List<View> results = with(createActivity(SimpleActivity.class)).find(View.class).allWhere(not(exists()));
        assertEquals(0, results.size());
    }

    @Test
    public void testFromComparable(){
        assertTrue(fromComparable(true).isMatch(true));
    }
}
