package dev.sergheev.commandbus.container;

/**
 * A typesafe heterogeneous container that maps types to its instances.
 *
 * <p>Unlike a regular container, the given class is parametrized. This
 * means that we are allowed to have different types for the keys, so
 * an instance can hold values of many (i.e. heterogeneous) types. The
 * type of token in the wildcard is used to guarantee that the type of
 * the key agrees with its value.
 *
 * @see <a href="http://www.informit.com/articles/article.aspx?p=2861454&seqNum=8">Joshua Bloch's Effective Java - Item 33
 */
public interface Container {

    /**
     * Associates the specified value with the type in this container.
     * If the key already had an associated value, the old value is
     * replaced by the specified value.
     *
     * @param key key with which the specified value is to be associated
     * @param value instance to be associated with the specified key
     * @param <T> the type of the value
     *
     * @return the given value associated with {@code key}
     */
    <T> T put(Class<T> key, Object value);

    /**
     * Removes the association for a key from this container if it is
     * present.
     *
     * @param key key whose association is to be removed from this
     *            container.
     * @param <T> the type of the value
     *
     * @return the previous value associated with {@code key}, or
     *         {@code null} if there was no association for {@code key}
     */
    <T> T remove(Class<T> key);

    /**
     * Returns the value to which the specified key is associated.
     *
     * @param key key whose associated value is to be returned
     * @param <T> the type of the value
     *
     * @return the current value associated with {@code key}, or
     *         {@code null} if there was no association for {@code key}
     */
    <T> T get(Class<T> key);

    /**
     * Returns {@code true} if this container has an association to an
     * instance for the specified key.
     *
     * @param key key whose presence in this container is to be tested
     *
     * @return {@code true} if this container has an association to an
     * instance for the specified key.
     */
    boolean contains(Class<?> key);

    /**
     * Removes all the associations and stored instances from the container.
     */
    void clear();

    /**
     * Returns {@code true} if this container has no stored instances.
     *
     * @return {@code true} if this container has no stored instances
     */
    boolean isEmpty();

    /**
     * Returns the amount of key-value associations in this container.
     *
     * @return the amount of key-value associations in this container
     */
    int size();

}
