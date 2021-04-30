package dev.sergheev.commandbus.container;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * A simple {@link Container} implementation.
 *
 * This implementation contains two creation methods, that provide
 * a way of choosing if we want a thread-safe or a non thread-safe
 * instance of the container.
 */
public class SimpleContainer implements Container {

    /**
     * Creates a thread-safe {@link SimpleContainer} based on a
     * {@link HashMap}.
     *
     * @return a thread-safe {@link SimpleContainer}
     */
    public static SimpleContainer newInstance() {
        return new SimpleContainer(HashMap::new);
    }

    /**
     * Creates a non thread-safe {@link SimpleContainer} based on
     * a {@link ConcurrentHashMap}.
     *
     * @return a non thread-safe {@link SimpleContainer}
     */
    public static SimpleContainer newConcurrentInstance() {
        return new SimpleContainer(ConcurrentHashMap::new);
    }

    private final Map<Class<?>, Object> typeToInstance;

    private SimpleContainer() {
        throw new AssertionError();
    }

    private SimpleContainer(Supplier<Map<Class<?>, Object>> mapSupplier) {
        this.typeToInstance = asEmptyMap(mapSupplier);
    }

    /*
     * Ensuring type-safety (by checking that the map has not been
     * modified externally) because Java is not capable of expressing
     * this key-value relationship.
     */
    private Map<Class<?>, Object> asEmptyMap(Supplier<Map<Class<?>, Object>> mapSupplier) {
        Map<Class<?>, Object> values = mapSupplier.get();
        final String message = "Supplied map must be empty";
        if(!values.isEmpty()) throw new IllegalArgumentException(message);
        return values;
    }

    @Override
    public <T> T put(Class<T> key, Object value) {
        requireNonNull(key, "key");
        requireNonNull(value, "value");
        return key.cast(typeToInstance.put(key, value));
    }

    @Override
    public <T> T remove(Class<T> key) {
        requireNonNull(key, "key");
        return key.cast(typeToInstance.remove(key));
    }

    @Override
    public <T> T get(Class<T> key) {
        requireNonNull(key, "key");
        return key.cast(typeToInstance.get(key));
    }

    @Override
    public boolean contains(Class<?> key) {
        requireNonNull(key, "key");
        return typeToInstance.containsKey(key);
    }

    @Override
    public void clear() {
        typeToInstance.clear();
    }

    @Override
    public boolean isEmpty() {
        return typeToInstance.isEmpty();
    }

    @Override
    public int size() {
        return typeToInstance.size();
    }

}