package dev.sergheev.commandbus.container;

/**
 * A base interface for an heterogeneous container.
 *
 * <p>Unlike a regular container, this container allows to have different types for the keys and
 * also to hold values of many (i.e. heterogeneous) types. The type of token is used to guarantee
 * that the type of the key agrees with its value.</p>
 *
 * @see <a href="http://www.informit.com/articles/article.aspx?p=2861454&seqNum=8">Joshua Bloch's Effective Java - Item 33
 */
public interface HeterogeneousContainer {

    /**
     * Associates the specified instance with the specified type.
     *
     * @param type type with which the specified instance is to be associated
     * @param instance instance to be associated with the specified type
     */
    void put(Class<?> type, Object instance);

    /**
     * Removes the mapping for a type from this container if it is present.
     *
     * @param type type whose mapping is to be removed from the container
     */
    void remove(Class<?> type);

    /**
     * Returns the instance to which the specified type is mapped.
     *
     * @param type type whose mapping is to be removed from the container.
     *
     * @return the instance to which the specified type is mapped
     */
    <T> T get(Class<T> type);

    /**
     * @return {@code true} if this container has no type-instance mappings.
     */
    boolean isEmpty();

    /**
     * @return the number of type-instance mappings
     */
    int size();

}
