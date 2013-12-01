package com.deftech.viewtils.matchers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/***
 * A base {@link Matcher} that should be extended by any other matchers.
 * @param <T> Type that the matcher is used for matching
 */
public abstract class BaseMatcher<T> implements Matcher<T> {

    public final T where(Requirement<? super T> requirement){
        Set<Requirement<? super T>> reqList = new HashSet<Requirement<? super T>>();
        reqList.add(requirement);
        return where(reqList);
    }

    public final List<T> allWhere(Requirement<? super T> requirement){
        Set<Requirement<? super T>> reqList = new HashSet<Requirement<? super T>>();
        reqList.add(requirement);
        return allWhere(reqList);
    }

    /***
     * Returns a requirement that all objects match
     * @return A requirement where {@code matchesRequirement} always returns {@code true}
     */
    public static Requirement<Object> any(){
        return new Requirement<Object>() {
            @Override public boolean matchesRequirement(Object instance) {
                return true;
            }
        };
    }

    /***
     * Returns a matcher for checkng that an instance is equal to
     * the provided instance, such that {@code instance.equals(otherInstance) == true}
     * @param instance Instance that any provided instance should be equal to
     * @return The created requirement
     */
    public static Requirement<Object> is(final Object instance){
        return new Requirement<Object>() {
            @Override public boolean matchesRequirement(Object i) {
                return instance.equals(i);
            }
        };
    }

    /***
     * Returns the first instance that doesn't match the provided requirement
     * @param req Requirement that the instance should not match
     * @param <T> Type that the requirement relates to
     * @return A requirement that matches anything that doesn't match the provided requirement
     */
    public static <T> Requirement<T> not(final Requirement<T> req){
        return new Requirement<T>() {
            @Override public boolean matchesRequirement(T instance) {
                return !req.matchesRequirement(instance);
            }
        };
    }

    /***
     * Helper method to create a {@link Requirement} from a provided {@link Comparable}. A
     * provided instance matches the returned requirement if {@code comparable.compareTo(instance) == 0}
     * @param comparable Comparable to be converted
     * @param <T> Type that the comparable checks
     * @return Requirement using the provided comparable
     */
    public static <T> Requirement<T> fromComparable(final Comparable<T> comparable){
        return new Requirement<T>() {
            @Override public boolean matchesRequirement(T instance) {
                return (comparable.compareTo(instance) == 0);
            }
        };
    }
}
