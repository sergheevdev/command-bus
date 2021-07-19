package dev.sergheev.commandbus.container;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * A typesafe heterogeneous {@link Container} implementation that maps
 * types to its instances.
 *
 * <p>Unlike a regular container, the given class is parametrized. This
 * means that we are allowed to have different types for the keys, so
 * an instance can hold values of many (i.e. heterogeneous) types. The
 * type of token in the wildcard is used to guarantee that the type of
 * the key agrees with its value.
 *
 * <p>This implementation contains two creation methods, that provide
 * a way of choosing if we want a thread-safe or a non thread-safe
 * instance of the container.
 */
public class SimpleContainer implements Container {

    /**
     * Creates a thread-safe {@link SimpleContainer} implementation based on a {@link HashMap}.
     * @return a thread-safe {@link SimpleContainer} instance
     */
    public static SimpleContainer newInstance() {
        return new SimpleContainer(HashMap::new);
    }

    /**
     * Creates a non thread-safe {@link SimpleContainer} implementation based on a {@link ConcurrentHashMap}.
     * @return a non thread-safe {@link SimpleContainer} instance
     */
    public static SimpleContainer newConcurrentInstance() {
        return new SimpleContainer(ConcurrentHashMap::new);
    }

    /**
     * Stores the type to instance associations.
     */
    private final Map<Class<?>, Object> typeToInstance;

    /**
     * @throws AssertionError if an attempt to instantiate {@code SimpleContainer} is made (ensures non-instantiability)
     */
    private SimpleContainer() {
        throw new AssertionError();
    }

    /**
     * Constructs a new {@link SimpleContainer} instance.
     * @throws NullPointerException if the {@code mapSupplier} is {@code null}
     * @throws NullPointerException if the {@code mapSupplier} supplied map is {@code null}
     * @throws IllegalArgumentException if the {@code mapSupplier} supplied map is not empty
     */
    private SimpleContainer(Supplier<Map<Class<?>, Object>> mapSupplier) throws NullPointerException, IllegalArgumentException {
        requireNonNull(mapSupplier, "mapSupplier must not be null");
        this.typeToInstance = asEmptyMap(mapSupplier);
    }

    /**
     * Ensuring type-safety (by checking that the map has not been modified externally)
     * because Java is not capable of expressing this key-value relationship.
     */
    private Map<Class<?>, Object> asEmptyMap(Supplier<Map<Class<?>, Object>> mapSupplier) throws NullPointerException, IllegalArgumentException {
        final Map<Class<?>, Object> suppliedMap = mapSupplier.get();
        requireNonNull(suppliedMap, "suppliedMap must not be null");
        if(!suppliedMap.isEmpty()) throw new IllegalArgumentException("suppliedMap must be empty");
        return suppliedMap;
    }

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
    @Override
    public <T> T put(Class<T> key, Object value) throws NullPointerException, IllegalArgumentException {
        requireNonNull(key, "key must not be null");
        requireNonNull(value, "value must not be null");
        if(!key.isInstance(value)) throw new IllegalArgumentException("The value instance must match the key type");
        return key.cast(typeToInstance.put(key, value));
    }

    /**
     * Removes the value associated to the given key from this container if present.
     * @param key the key whose association is to be removed from this container.
     * @param <T> the type of the value that is associated with the specified key
     * @throws NullPointerException if the {@code key} is {@code null}
     * @return the previous value associated with {@code key}, or {@code null}
     *         if there was no associated value to {@code key}
     */
    @Override
    public <T> T remove(Class<T> key) throws NullPointerException {
        requireNonNull(key, "key must not be null");
        return key.cast(typeToInstance.remove(key));
    }

    /**
     * Returns the value to which the specified key is currently associated to.
     * @param key the key whose associated value is to be returned
     * @param <T> the type of the value that is associated with the specified key
     * @throws NullPointerException if the {@code key} is {@code null}
     * @return the current value associated with {@code key}, or {@code null}
     *         if there is no {@code value} associated to the given {@code key}
     */
    @Override
    public <T> T get(Class<T> key) throws NullPointerException {
        requireNonNull(key, "key must not be null");
        return key.cast(typeToInstance.get(key));
    }

    /**
     * Returns {@code true} if this container has an associated value to the
     * given key.
     * @param key the key whose presence in this container is to be tested
     * @throws NullPointerException if the {@code key} is {@code null}
     * @return {@code true} if this container has an associated {@code value}
     *         to the specified {@code key}, {@code false} otherwise
     */
    @Override
    public boolean contains(Class<?> key) throws NullPointerException {
        requireNonNull(key, "key must not be null");
        return typeToInstance.containsKey(key);
    }

    /**
     * Removes all the key-value associations from this container.
     */
    @Override
    public void clear() {
        typeToInstance.clear();
    }

    /**
     * Returns {@code true} if this container has no key-value stored associations.
     * @return {@code true} if this container has no key-value stored associations, {@code false} otherwise
     */
    @Override
    public boolean isEmpty() {
        return typeToInstance.isEmpty();
    }

    /**
     * Returns the amount of key-value associations in this container.
     * @return the amount of key-value associations in this container
     */
    @Override
    public int size() {
        return typeToInstance.size();
    }

}