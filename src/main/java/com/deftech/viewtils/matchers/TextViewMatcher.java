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
                 return t.getText().toString().matches(regex);
            }
        };
    }

    public static Requirement<TextView> textIs(final int id){
        return new Requirement<TextView>() {
            @Override public boolean matchesRequirement(TextView t) {
                return t.getText().toString().equals(t.getContext().getString(id));
            }
        };
    }
}
