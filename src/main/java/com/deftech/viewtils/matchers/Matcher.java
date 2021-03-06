package com.deftech.viewtils.matchers;

import java.util.List;

/***
 * A Matcher is used for getting an object from a source that
 * matches a provided {@link Requirement}
 * @param <T> Type of the object to be retrieved
 */
public interface Matcher<T> {

    /***
     * Returns the first object that matches the provided requirement and
     * is of the required type
     * @param requirement requirement that the object must meet
     * @return first object that matches the requirement, or null if no
     * objects match it
     */
    public T where(Requirement<? super T> requirement);

    /***
     * Returns all objects that match the provided requirement and
     * are of the required type
     * @param requirement requirement that the objects must meet
     * @return list containing all objects that match the requirement
     */
    public List<T> allWhere(Requirement<? super T> requirement);
}
