package com.deftech.viewtils.matchers;


public abstract class BaseMatcher<T> implements Matcher<T> {
    public final T is(final Object instance){
        return matches(new Requirement<Object>(){
            @Override public boolean match(Object i) {
                return instance.equals(i);
            }
        });
    }
}
