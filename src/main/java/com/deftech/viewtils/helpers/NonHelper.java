package com.deftech.viewtils.helpers;

import com.deftech.viewtils.matchers.Matcher;
import com.deftech.viewtils.matchers.Requirement;

import java.util.ArrayList;
import java.util.List;


class NonHelper<T,C> extends Helper<T,C> {

    public NonHelper(Class<T> instanceClass){
        super(instanceClass);
    }

    public NonHelper(T instance){
        super(instance);
    }

    @Override
    public <T extends C> Matcher<T> find(Class<T> instanceClass) {
        return new NonMatcher<T>();
    }

    @Override
    public <T extends C> Matcher<T> click(Class<T> instanceClass) {
        return new NonMatcher<T>();
    }

    /***
     * A matcher that always returns no matches
     * @param <T> The type of the matcher
     */
    private static final class NonMatcher<T> implements Matcher<T> {

        @Override
        public T where(Requirement<? super T> requirement) {
            return null;
        }

        @Override
        public List<T> allWhere(Requirement<? super T> requirement) {
            return new ArrayList<T>();
        }
    }
}
