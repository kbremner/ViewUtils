package com.deftech.viewtils.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
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
import static com.deftech.viewtils.helpers.Helper.with;
import static com.deftech.viewtils.matchers.ViewMatcher.*;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/test/resources/AndroidManifest.xml")
public class ViewMatcherTest {

    @Test
    public void testFindTextView() {
        TextView view = with(createActivity(SimpleActivity.class)).find(TextView.class).where(textIs("New Text"));
        assertNotNull(view);
        assertEquals("New Text", view.getText().toString());
    }

    @Test
    public void testFindTextViewUsingViewGroupHelper() {
        Activity activity = createActivity(SimpleActivity.class);
        ViewGroup viewGroup = (ViewGroup) activity.findViewById(android.R.id.content);
        TextView view = with(viewGroup).find(TextView.class).where(textIs("New Text"));
        assertNotNull(view);
        assertEquals("New Text", view.getText().toString());
    }

    @Test
    public void testFindTextViewEmptyContent() {
        // Set the text to empty
        Activity activity = createActivity(SimpleActivity.class);
        TextView view = with(activity).find(TextView.class).where(idIs(R.id.textView));
        view.setText(null);

        // Find the text view with null content
        view = with(activity).find(TextView.class).where(textIs(""));
        assertEquals("", view.getText());
    }

    @Test
    public void testFindTextViewWithId() {
        TextView view = with(createActivity(SimpleActivity.class)).find(TextView.class).where(idIs(R.id.textView));
        assertNotNull(view);
        assertEquals(R.id.textView, view.getId());
    }

    @Test
    public void testDontFindTextViewWithId() {
        assertNull(with(createActivity(SimpleActivity.class)).find(TextView.class).where(idIs(0)));
    }

    @Test
    public void testFindTextViewWithCustomReq() {
        TextView view = with(createActivity(SimpleActivity.class)).find(TextView.class).where(new Requirement<View>() {
            @Override
            public boolean isMatch(View instance) {
                return instance.getVisibility() == View.VISIBLE;
            }
        });
        assertNotNull(view);
        assertEquals(View.VISIBLE, view.getVisibility());
    }

    @Test
    public void testFindTextViewsMatchesAll(){
        Set<Requirement<? super TextView>> reqs = new HashSet<Requirement<? super TextView>>();
        reqs.add(textIs(R.string.tv_str));
        reqs.add(idIs(R.id.textView));
        List<TextView> result = with(createActivity(SimpleActivity.class)).find(TextView.class).allWhere(matchesAll(reqs));

        assertEquals(1, result.size());
        assertEquals("New Text", result.get(0).getText().toString());
    }

    @Test
    public void testDontFindTextViewsMatchesAll(){
        Set<Requirement<? super TextView>> reqs = new HashSet<Requirement<? super TextView>>();
        reqs.add(textIs(R.string.tv_str));
        reqs.add(idIs(R.id.button));
        List<TextView> result = with(createActivity(SimpleActivity.class)).find(TextView.class).allWhere(matchesAll(reqs));
        assertEquals(0, result.size());
    }

    @Test
    public void testFindTextViewsMatchesAny(){
        Activity activity = createActivity(SimpleActivity.class);
        Set<Requirement<? super TextView>> reqs = new HashSet<Requirement<? super TextView>>();
        reqs.add(textIs(R.string.tv_str));
        reqs.add(idIs(0)); // Purposefully wrong...
        List<TextView> result = with(activity).find(TextView.class).allWhere(matchesAny(reqs));

        assertEquals(1, result.size());
        assertEquals(activity.getString(R.string.tv_str), result.get(0).getText().toString());
    }

    @Test
    public void testDontFindTextViewsMatchesAny(){
        Set<Requirement<? super TextView>> reqs = new HashSet<Requirement<? super TextView>>();
        reqs.add(textIs("Some other text string"));
        reqs.add(idIs(0));
        List<TextView> result = with(createActivity(SimpleActivity.class)).find(TextView.class).allWhere(matchesAny(reqs));
        assertEquals(0, result.size());
    }

    @Test
    public void testFindAllTextViewsWithId(){
        List<TextView> results = with(createActivity(SimpleActivity.class)).find(TextView.class).allWhere(idIs(R.id.textView));
        assertEquals(1, results.size());  // TextView
    }

    @Test
    public void testFindAllTextViews(){
        List<TextView> results = with(createActivity(SimpleActivity.class)).find(TextView.class).allWhere(exists());
        assertEquals(2, results.size());  // Button, TextView
    }



    /* -------- Button tests --------- */
    @Test
    public void testFindButton() {
        Button view = with(createActivity(SimpleActivity.class)).find(Button.class).where(textIs("New Button"));
        assertNotNull(view);
        assertEquals("New Button", view.getText().toString());
    }

    @Test
    public void testFindButtonWithId() {
        Button view = with(createActivity(SimpleActivity.class)).find(Button.class).where(idIs(R.id.button));
        assertNotNull(view);
        assertEquals(R.id.button, view.getId());
    }

    @Test
    public void testFindButtonWithCustomReq() {
        Button view = with(createActivity(SimpleActivity.class)).find(Button.class).where(new Requirement<View>() {
            @Override public boolean isMatch(View instance) {
                return instance.getVisibility() == View.VISIBLE;
            }
        });
        assertNotNull(view);
        assertEquals(View.VISIBLE, view.getVisibility());
    }

    @Test
    public void testFindButtonMatchesRegex() {
        Activity activity = createActivity(SimpleActivity.class);
        Button view = with(activity).find(Button.class).where(textMatches(".*Button"));
        assertNotNull(view);
        assertEquals(activity.getString(R.string.btn_str), view.getText().toString());
    }

    @Test
    public void testFindButtonMatchesEmptyContent() {
        // Set the text to empty
        Activity activity = createActivity(SimpleActivity.class);
        TextView view = with(activity).find(TextView.class).where(idIs(R.id.button));
        view.setText(null);

        // Find the text view with null content
        view = with(activity).find(TextView.class).where(textMatches(""));
        assertEquals("", view.getText());
    }

    @Test
    public void testFindAllButtons(){
        List<Button> results = with(createActivity(SimpleActivity.class)).find(Button.class).allWhere(exists());
        assertEquals(1, results.size());  // Button
    }

    @Test
    public void testFindButtonWithStringId() {
        Activity activity = createActivity(SimpleActivity.class);
        Button view = with(activity).find(Button.class).where(textIs(R.string.btn_str));
        assertNotNull(view);
        assertEquals(activity.getString(R.string.btn_str), view.getText().toString());
    }

    @Test
    public void testDontFindTextViewWithStringId() {
        assertNull(with(createActivity(SimpleActivity.class)).find(Button.class).where(textIs(R.string.tv_str)));
    }

    @Test
    public void testClickTextView(){
        SimpleActivity activity = createActivity(SimpleActivity.class);
        Button button = with(activity).find(Button.class).where(idIs(R.id.button));
        // There are 2 text views, but only R.id.button has an onClick listener
        assertEquals(button, with(activity).click(TextView.class).where(exists()));
        assertTrue(activity.isViewClicked());
    }

    @Test
    public void testClickButton(){
        SimpleActivity activity = createActivity(SimpleActivity.class);
        Button button = with(activity).find(Button.class).where(idIs(R.id.button));
        assertEquals(button, with(activity).click(Button.class).where(exists()));
        assertTrue(activity.isViewClicked());
    }

    @Test
    public void testClickTextViewWithContent() {
        SimpleActivity activity = createActivity(SimpleActivity.class);
        TextView button = with(activity).find(TextView.class).where(idIs(R.id.button));
        assertEquals(button, with(activity).click(TextView.class).where(textIs(R.string.btn_str)));
        assertTrue(activity.isViewClicked());
    }

    @Test
    public void testDontClick() {
        assertNull(with(createActivity(SimpleActivity.class)).click(TextView.class).where(not(exists())));
    }

    @Test
    public void testDontClickTextViewWithContentNoOnClick() {
        /*
         * Helper will find the class, but view.performClick() will return false
         * because there is no onClick listener
         */
        assertNull(with(createActivity(SimpleActivity.class)).click(TextView.class).where(textIs(R.string.tv_str)));
    }



    /* -------- ViewGroup tests --------- */
    @Test
    public void testFindViewGroupWithId(){
        Activity activity = createActivity(SimpleActivity.class);
        ViewGroup group = with(activity).find(ViewGroup.class).where(idIs(R.id.layout));
        assertNotNull(group);
        assertEquals(activity.findViewById(R.id.layout), group);
    }

    @Test
    public void testClickViewWithSpinner(){
        Activity activity = createActivity(SpinnerActivity.class);
        Spinner spinner = with(activity).find(Spinner.class).where(idIs(R.id.spinner));
        assertEquals(0, spinner.getSelectedItemPosition());

        TextView spinnerItem = with(spinner).click(TextView.class).where(textIs("Item 3"));
        assertNotNull(spinnerItem);
        assertEquals(2, spinner.getSelectedItemPosition());
        assertEquals("Item 3", spinnerItem.getText().toString());
    }

    @Test
    public void testClickViewInSpinner(){
        Activity activity = createActivity(SpinnerActivity.class);

        // Get the spinner for checking and ensure first item is currently selected
        Spinner spinner = with(activity).find(Spinner.class).where(idIs(R.id.spinner));
        assertEquals(0, spinner.getSelectedItemPosition());

        // Click the appropriate view and check that it was selected
        TextView spinnerItem = with(activity).click(TextView.class).where(textIs("Item 2"));
        assertNotNull(spinnerItem);
        assertEquals("Item 2", spinnerItem.getText().toString());
        assertEquals(1, spinner.getSelectedItemPosition());
    }
    
    @Test
    public void testDontClickViewInSpinner(){
        Activity activity = createActivity(SpinnerActivity.class);

        // Get the spinner for checking and ensure first item is currently selected
        Spinner spinner = with(activity).find(Spinner.class).where(idIs(R.id.spinner));
        assertEquals(0, spinner.getSelectedItemPosition());

        // Try and click a non-existant item and check that nothing was selected
        TextView spinnerItem = with(activity).click(TextView.class).where(textIs("Non-existant Item"));
        assertNull(spinnerItem);
        assertEquals(0, spinner.getSelectedItemPosition());
    }

    @Test
    public void testFindViewInSpinner(){
        Activity activity = createActivity(SpinnerActivity.class);

        // Get the spinner for checking and ensure first item is currently selected
        Spinner spinner = with(activity).find(Spinner.class).where(idIs(R.id.spinner));
        assertEquals(0, spinner.getSelectedItemPosition());

        // Find the appropriate view and check that it was *not* selected
        TextView spinnerItem = with(activity).find(TextView.class).where(textIs("Item 2"));
        assertNotNull(spinnerItem);
        assertEquals("Item 2", spinnerItem.getText().toString());
        assertEquals(0, spinner.getSelectedItemPosition());
    }

    @Test
    public void testSpinnerNoAdapter(){
        Spinner spinner = new Spinner(createActivity(SimpleActivity.class));
        assertNull(spinner.getAdapter());
        TextView spinnerItem = with(spinner).find(TextView.class).where(textIs("Item 2"));
        assertNull(spinnerItem);
    }

    @Test
    public void testFindInDialog(){
        String someMsg = "Some Message";

        // Create a new dialog to search in, with the message as content
        Dialog d = new AlertDialog.Builder(createActivity(SimpleActivity.class))
                .setMessage(someMsg)
                .create();

        // Has to be showing for search to work
        d.show();

        // Search for a text view with the message and check we got a result
        TextView result = with(d).find(TextView.class).where(textIs(someMsg));
        assertNotNull(result);
        assertEquals(someMsg, result.getText().toString());
    }

    @Test
    public void testDontFindInDialog(){
        String someMsg = "Some Message";

        // Create a new dialog to search in, with the message as content
        Dialog d = new AlertDialog.Builder(createActivity(SimpleActivity.class))
                .setMessage("Some other message")
                .create();

        // Has to be showing for search to work
        d.show();

        // Search for a text view with the message and check we *didn't* get a result
        TextView result = with(d).find(TextView.class).where(textIs(someMsg));
        assertNull(result);
    }
}
