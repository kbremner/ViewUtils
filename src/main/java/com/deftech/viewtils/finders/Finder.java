package com.deftech.viewtils.finders;

import com.deftech.viewtils.matchers.BaseMatcher;

public interface Finder<T> {
    public BaseMatcher<T> where();
}
