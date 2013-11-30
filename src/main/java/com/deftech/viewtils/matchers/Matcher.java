package com.deftech.viewtils.matchers;

/***
 * A Matcher is used for getting an object from a source that
 * matches a provided {@link Requirement}
 * @param <T> Type of the object to be retrieved
 */
public interface Matcher<T> {
    /***
     * Returns the first object that matches the provided requirement
     * @param requirement requirement that the object should meet
     * @return first object that matches the requirement, or null if no
     * objects match it
     */
    public T where(Requirement<? super T> requirement);
}
