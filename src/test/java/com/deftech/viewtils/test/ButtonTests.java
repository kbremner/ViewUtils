package com.deftech.viewtils.test;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import com.deftech.viewtils.matchers.Requirement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static com.deftech.viewtils.matchers.BaseMatcher.any;
import static com.deftech.viewtils.test.TestUtil.createActivity;
import static org.robolectric.Robolectric.buildActivity;
import static com.deftech.viewtils.Helper.with;
import static com.deftech.viewtils.matchers.ViewMatcher.idIs;
import static com.deftech.viewtils.matchers.ViewMatcher.textIs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/test/resources/AndroidManifest.xml")
public class ButtonTests {
    @Test
    public void testFindButton() throws Exception {
        Activity activity = buildActivity(SimpleActivity.class).create().get();
        Button view = with(activity).find(Button.class).where(textIs("New Button"));
        assertNotNull(view);
        assertEquals(view.getText().toString(), "New Button");
    }

    @Test
    public void testFindButtonWithId() throws Exception {
        Activity activity = buildActivity(SimpleActivity.class).create().get();
        Button view = with(activity).find(Button.class).where(idIs(R.id.button));
        assertNotNull(view);
        assertEquals(view.getId(), R.id.button);
    }

    @Test
    public void testFindButtonWithCustomReq() throws Exception {
        Activity activity = buildActivity(SimpleActivity.class).create().get();
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
    public void testFindAllButtons(){
        List<Button> results = with(createActivity()).find(Button.class).allWhere(any());
        assertEquals(results.size(), 1);  // Button
    }
}
