package com.deftech.viewtils.matchers;


public interface Requirement<T> {
    public boolean match(T instance);
}
