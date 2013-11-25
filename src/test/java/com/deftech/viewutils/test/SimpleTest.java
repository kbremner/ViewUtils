package com.deftech.viewutils.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.deftech.viewutils.ViewUtils;

import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;



@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/test/resources/AndroidManifest.xml")
public class SimpleTest {

  @org.junit.Test
  public void testSomething() throws Exception {
    Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
    assertTrue(activity != null);
  }

  @org.junit.Test
  public void testFindTextView() throws Exception {
    Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
    TextView view = ViewUtils.findViewWithContent(activity, "New Text", TextView.class);
    System.out.println(view.getText());
    assertNotNull(view);
  }

    @org.junit.Test
    public void testFindButton() throws Exception {

        Activity activity = Robolectric.buildActivity(SimpleActivity.class).create().get();
        Button view = ViewUtils.findViewWithContent(activity, "New Button", Button.class);
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
