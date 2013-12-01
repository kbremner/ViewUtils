package com.deftech.viewtils.test;

import android.view.View;
import android.widget.Button;
import com.deftech.viewtils.matchers.Requirement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static com.deftech.viewtils.matchers.BaseMatcher.exists;
import static com.deftech.viewtils.test.TestUtil.createActivity;
import static org.junit.Assert.assertNull;
import static com.deftech.viewtils.Helper.with;
import static com.deftech.viewtils.matchers.ViewMatcher.idIs;
import static com.deftech.viewtils.matchers.ViewMatcher.textIs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/test/resources/AndroidManifest.xml")
public class ButtonTest {
    @Test
    public void testFindButton() throws Exception {
        Button view = with(createActivity()).find(Button.class).where(textIs("New Button"));
        assertNotNull(view);
        assertEquals(view.getText().toString(), "New Button");
    }

    @Test
    public void testFindButtonWithId() throws Exception {
        Button view = with(createActivity()).find(Button.class).where(idIs(R.id.button));
        assertNotNull(view);
        assertEquals(view.getId(), R.id.button);
    }

    @Test
    public void testFindButtonWithCustomReq() throws Exception {
        Button view = with(createActivity()).find(Button.class).where(new Requirement<View>() {
            @Override public boolean matchesRequirement(View instance) {
                return instance.getVisibility() == View.VISIBLE;
            }
        });
        assertNotNull(view);
        assertEquals(view.getVisibility(), View.VISIBLE);
    }

    @Test
    public void testFindAllButtons(){
        List<Button> results = with(createActivity()).find(Button.class).allWhere(exists());
        assertEquals(results.size(), 1);  // Button
    }

    @Test
    public void testFindButtonWithStringId() throws Exception {
        Button view = with(createActivity()).find(Button.class).where(textIs(R.string.btn_str));
        assertNotNull(view);
        assertEquals(view.getText().toString(), view.getContext().getString(R.string.btn_str));
    }

    @Test
    public void testDontFindTextViewWithStringId() throws Exception {
        assertNull(with(createActivity()).find(Button.class).where(textIs(R.string.tv_str)));
    }
}
