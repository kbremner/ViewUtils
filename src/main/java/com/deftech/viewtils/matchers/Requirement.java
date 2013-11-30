package com.deftech.viewtils.matchers;


public interface Requirement<T> {
    public boolean matchesRequirement(T instance);
}
