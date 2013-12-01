package com.deftech.viewtils.matchers;

/***
 * Represents a requirement for an object of type {@code T}
 * @param <T> The type of object that the requirement relates to
 */
public interface Requirement<T> {
    /***
     * Tests whether {@code instance} matches this requirement
     * @param instance instance to test
     * @return true if the instance matches this requirement, else false
     */
    public boolean matchesRequirement(T instance);
}
