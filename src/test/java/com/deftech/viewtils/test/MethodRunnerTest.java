package com.deftech.viewtils.test;

import android.os.Handler;
import android.os.Looper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.deftech.viewtils.Helper.with;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/test/resources/AndroidManifest.xml")
public class MethodRunnerTest {
    private static boolean methodRan = false;

    public void simpleMethod(){
        methodRan = true;
    }

    public static void simpleStaticMethod(){
        methodRan = true;
    }

    public void simpleExceptionMethod(){
        throw new IndexOutOfBoundsException();
    }

    public void simpleCheckedExceptionMethod() throws IOException {
        throw new IOException();
    }

    public String simpleMethod(String param1, String param2){
        return param1 + param2;
    }

    public boolean simpleMethodReturningPrimitive() {
        return true;
    }

    private void simplePrivateMethod(){
        methodRan = true;
    }



    @Test
    public void testMethod(){
        methodRan = false;
        with(this).executeOnUiThread("simpleMethod")
                .usingRobolectric()
                .returningNothing();

        assertTrue(methodRan);
    }

    @Test
    public void testExplicitVoidReturnType(){
        methodRan = false;
        with(this).executeOnUiThread("simpleMethod")
                .usingRobolectric()
                .returning(void.class);
        assertTrue(methodRan);
    }

    @Test
    public void testDelayed(){
        methodRan = false;
        with(this).executeOnUiThread("simpleMethod")
                .usingRobolectric()
                .in(200, TimeUnit.MILLISECONDS)
                .returningNothing();
        assertTrue(methodRan);
    }

    @Test
    public void testDifferentHandler(){
        methodRan = false;
        with(this).executeOnUiThread("simpleMethod")
                .usingRobolectric()
                .withHandler(new Handler(Looper.getMainLooper()))
                .returningNothing();
        assertTrue(methodRan);
    }

    @Test
    public void testRunStaticMethod(){
        methodRan = false;
        with(MethodRunnerTest.class).executeOnUiThread("simpleStaticMethod")
            .usingRobolectric()
            .returningNothing();
            
        assertTrue("Static method didn't run", methodRan);
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

    @Test(expected = IOException.class)
    public void testCheckedExceptionThrown() throws Throwable {
        try {
            with(this).executeOnUiThread("simpleCheckedExceptionMethod")
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

    @Test(expected = NoSuchMethodException.class)
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

    @Test
    public void testPrimitiveReturnType() {
        boolean result = with(this).executeOnUiThread("simpleMethodReturningPrimitive")
                .usingRobolectric()
                .returning(Boolean.class);

        assertTrue(result);
    }

    @Test(expected = NoSuchMethodException.class)
    public void testPrivateMethod() throws Throwable {
        try {
            with(this).executeOnUiThread("simplePrivateMethod")
                    .usingRobolectric()
                    .returningNothing();
        } catch(RuntimeException e){
            throw (e.getCause() == null) ? e : e.getCause(); // MethodRunner wraps all exceptions, need to unwrap
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullReturnType() throws Throwable {
        try {
            with(this).executeOnUiThread("simpleMethod")
                    .usingRobolectric()
                    .returning(null);
        } catch(RuntimeException e){
            throw (e.getCause() == null) ? e : e.getCause(); // MethodRunner wraps all exceptions, need to unwrap
        }
    }
}
