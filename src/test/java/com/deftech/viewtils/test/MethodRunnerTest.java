package com.deftech.viewtils.test;


import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import com.deftech.viewtils.matchers.TextViewMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.TimeUnit;

import static com.deftech.viewtils.Helper.with;
import static com.deftech.viewtils.test.TestUtil.createActivity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/test/resources/AndroidManifest.xml")
public class MethodRunnerTest {
    private static boolean methodRan = false;
    
    @Test
    public void testSetText() {
        Activity activity = createActivity();

        // Get the view
        TextView view = with(activity).find(TextView.class).where(TextViewMatcher.textIs(R.string.tv_str));
        assertNotNull(view);
        assertEquals(view.getText().toString(), view.getContext().getString(R.string.tv_str));

        // Change the text
        with(view).executeOnUiThread("setText")
                .withParameter("Set text", CharSequence.class)
                .usingRobolectric()
                .returningNothing();

        // Validate that the change happened
        view = with(activity).find(TextView.class).where(TextViewMatcher.textIs("Set text"));
        assertNotNull(view);
        assertEquals(view.getText().toString(), "Set text");
    }

    @Test
    public void testDelayedSetText() {
        Activity activity = createActivity();

        // Get the view
        TextView view = with(activity).find(TextView.class).where(TextViewMatcher.textIs(R.string.tv_str));
        assertNotNull(view);
        assertEquals(view.getText().toString(), view.getContext().getString(R.string.tv_str));

        // Change the text
        with(view).executeOnUiThread("setText")
                .withParameter("Set text", CharSequence.class)
                .usingRobolectric()
                .in(1000, TimeUnit.MILLISECONDS) // wait a second
                .returningNothing();

        // Validate that the change happened
        view = with(activity).find(TextView.class).where(TextViewMatcher.textIs("Set text"));
        assertNotNull(view);
        assertEquals(view.getText().toString(), "Set text");
    }

    @Test
    public void testSetTextWithDifferentHandler(){
        Activity activity = createActivity();

        // Get the view
        TextView view = with(activity).find(TextView.class).where(TextViewMatcher.textIs(R.string.tv_str));
        assertNotNull(view);
        assertEquals(view.getText().toString(), view.getContext().getString(R.string.tv_str));

        // Change the text
        with(view).executeOnUiThread("setText")
                .withParameter("Set text", CharSequence.class)
                .withHandler(new Handler(Looper.getMainLooper()))
                .usingRobolectric()
                .returningNothing();

        // Validate that the change happened
        view = with(activity).find(TextView.class).where(TextViewMatcher.textIs("Set text"));
        assertNotNull(view);
        assertEquals(view.getText().toString(), "Set text");
    }

    public static void simpleStaticMethod(){
        methodRan = true;
    }

    @Test
    public void testRunStaticMethod(){
        methodRan = false;
        with(MethodRunnerTest.class).executeOnUiThread("simpleStaticMethod")
            .usingRobolectric()
            .returningNothing();
            
        assertTrue("Static method didn't run", methodRan);
    }


    public void simpleExceptionMethod(){
        throw new IndexOutOfBoundsException("Expected this to be thrown");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testExceptionThrown() throws Throwable {
        try {
            with(this).executeOnUiThread("simpleExceptionMethod")
                    .usingRobolectric()
                    .returningNothing();
        } catch(RuntimeException e){
            throw e.getCause(); // MethodRunner wraps all exceptions, need to unwrap
        }
    }

    @Test(expected = NoSuchMethodException.class)
    public void testNonExistantMethod() throws Throwable {
        try {
            with(this).executeOnUiThread("someMethodThatDoesntExist")
                    .usingRobolectric()
                    .returningNothing();
        } catch(RuntimeException e){
            throw e.getCause(); // MethodRunner wraps all exceptions, need to unwrap
        }
    }

    public String simpleMethod(String param1, String param2){
        return param1 + param2;
    }

    @Test
    public void testWithParameters(){
        String result = with(this).executeOnUiThread("simpleMethod")
                .withParameter("param1", String.class)
                .withParameter("param2", String.class)
                .returning(String.class);

        assertEquals(result, "param1param2");
    }

    @Test(expected = NoSuchMethodException.class)
    public void testTooFewParameters() throws Throwable {
        try {
            with(this).executeOnUiThread("simpleMethod")
                    .withParameter("param1", String.class)
                    .usingRobolectric()
                    .returningNothing();
        } catch(RuntimeException e){
            throw (e.getCause() == null) ? e : e.getCause(); // MethodRunner wraps all exceptions, need to unwrap
        }
    }

    @Test(expected = NoSuchMethodException.class)
    public void testIncorrectParameterType() throws Throwable {
        try {
            with(this).executeOnUiThread("simpleMethod")
                    .withParameter("param1", String.class)
                    .withParameter("param2", CharSequence.class)
                    .usingRobolectric()
                    .returningNothing();
        } catch(RuntimeException e){
            throw (e.getCause() == null) ? e : e.getCause(); // MethodRunner wraps all exceptions, need to unwrap
        }
    }

    @Test(expected = ClassCastException.class)
    public void testIncorrectReturnType() throws Throwable {
        try {
            with(this).executeOnUiThread("simpleMethod")
                    .withParameter("param1", String.class)
                    .withParameter("param2", String.class)
                    .usingRobolectric()
                    .returning(Integer.class);

        } catch(RuntimeException e){
            throw (e.getCause() == null) ? e : e.getCause(); // MethodRunner wraps all exceptions, need to unwrap
        }
    }

    public boolean simpleMethodReturningAPrimitive() {
        return true;
    }

    @Test
    public void testPrimitiveReturnType() {
        boolean result = with(this).executeOnUiThread("simpleMethodReturningAPrimitive")
                .usingRobolectric()
                .returning(Boolean.class);

        assertTrue(result);
    }
}
