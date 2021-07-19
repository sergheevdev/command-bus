package dev.sergheev.commandbus.container;

/**
 * A typesafe heterogeneous container that maps types to its instances.
 *
 * <p>Unlike a regular container, the given class is parametrized. This
 * means that we are allowed to have different types for the keys, so
 * an instance can hold values of many (i.e. heterogeneous) types. The
 * type of token in the wildcard is used to guarantee that the type of
 * the key agrees with its value.
 */
public interface Container {

    /**
     * Associates the specified key with the provided value in this container.
     * If the key already had an associated value to it, the old value is
     * replaced by the new value.
     * @param key the key with which the specified value is to be associated
     * @param value the instance to be associated with the specified key
     * @param <T> the type of the value
     * @throws NullPointerException if the {@code key} is {@code null}
     * @throws NullPointerException if the {@code value} is {@code null}
     * @throws IllegalArgumentException if the {@code value} is not an instance of the {@code key} type
     * @return the previous associated {@code value} with {@code key}, or
     *         {@code null} if there was no associated {@code value} for
     *         {@code key}
     */
    <T> T put(Class<T> key, Object value) throws NullPointerException, IllegalArgumentException;

    /**
     * Removes the value associated to the given key from this container if present.
     * @param key the key whose association is to be removed from this container.
     * @param <T> the type of the value that is associated with the specified key
     * @throws NullPointerException if the {@code key} is {@code null}
     * @return the previous value associated with {@code key}, or {@code null}
     *         if there was no associated value to {@code key}
     */
    <T> T remove(Class<T> key) throws NullPointerException;

    /**
     * Returns the value to which the specified key is currently associated to.
     * @param key the key whose associated value is to be returned
     * @param <T> the type of the value that is associated with the specified key
     * @throws NullPointerException if the {@code key} is {@code null}
     * @return the current value associated with {@code key}, or {@code null}
     *         if there is no {@code value} associated to the given {@code key}
     */
    <T> T get(Class<T> key) throws NullPointerException;

    /**
     * Returns {@code true} if this container has an associated value to the
     * given key.
     * @param key the key whose presence in this container is to be tested
     * @throws NullPointerException if the {@code key} is {@code null}
     * @return {@code true} if this container has an associated {@code value}
     *         to the specified {@code key}, {@code false} otherwise
     */
    boolean contains(Class<?> key) throws NullPointerException;

    /**
     * Removes all the key-value associations from this container.
     */
    void clear();

    /**
     * Returns {@code true} if this container has no key-value stored associations.
     * @return {@code true} if this container has no key-value stored associations,
     *         {@code false} otherwise
     */
    boolean isEmpty();

    /**
     * Returns the amount of key-value associations in this container.
     * @return the amount of key-value associations in this container
     */
    int size();

}
