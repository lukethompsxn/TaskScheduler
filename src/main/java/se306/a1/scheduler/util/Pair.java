package se306.a1.scheduler.util;

import java.util.Objects;

/**
 * Generic utility class used to store pairs of objects.
 *
 * @param <T> the type of the first element in the pair
 * @param <U> the type of the second element in the pair
 */
public class Pair<T, U> {
    private T first;
    private U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Retrieve the first element in the pair.
     *
     * @return the first element in the pair
     */
    public T first() {
        return first;
    }

    /**
     * Retrieve the second element in the pair.
     *
     * @return the second element in the pair
     */
    public U second() {
        return second;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Pair))
            return false;

        Pair p = (Pair) other;
        return Objects.equals(first, p.first) && Objects.equals(second, p.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
