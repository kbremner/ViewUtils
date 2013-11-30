package com.deftech.viewtils.matchers;

public interface Matcher<T> {
    public T matches(Requirement<? super T> requirement);
}
