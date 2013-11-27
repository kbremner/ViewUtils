package com.deftech.viewtils.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static com.deftech.viewtils.helpers.Helper.with;



@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/test/resources/AndroidManifest.xml")
public class SimpleTest {

    @org.junit.Test
    public void testFindTextView() throws Exception {
        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
        TextView view = with(activity).find(TextView.class).where("New Text");
        // TODO:
        // 1 - Support returning all results
        // 2 - Support matchers, i.e. TextView view = with(activity).find(TextView.class).where(textIs("New Text"));
        System.out.println(view.getText());
        assertNotNull(view);
    }

    @org.junit.Test
    public void testFindButton() throws Exception {

        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
        Button view = with(activity).find(Button.class).where("New Button");
        System.out.println(view.getText());
        assertNotNull(view);
    }



    public static final class SimpleActivity extends Activity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main_layout);
        }
    }
}
