package com.deftech.viewtils.matchers;

import android.view.ViewGroup;
import android.widget.TextView;


public class TextViewMatcher<T extends TextView> extends ViewMatcher<T> {
    public TextViewMatcher(ViewGroup group, Class<T> viewClass) {
        super(group, viewClass);
    }

    public static Requirement<TextView> textIs(final String content){
        return new Requirement<TextView>() {
            @Override public boolean matchesRequirement(TextView t) {
                return t.getText().toString().equals(content);
            }
        };
    }

    public static Requirement<TextView> textMatches(final String regex){
        return new Requirement<TextView>(){
            @Override public boolean matchesRequirement(TextView t){
                if(t.getText() == null){
                    return regex == null;
                }
                return (t.getText() == null) ? (regex == null) : t.getText().toString().matches(regex);
            }
        };
    }

    public static Requirement<TextView> textIs(final int id){
        return new Requirement<TextView>() {
            @Override public boolean matchesRequirement(TextView t) {
                    String content = t.getContext().getString(id);
                    return (content != null && t.getText() != null) && content.equals(t.getText().toString());
            }
        };
    }
}
