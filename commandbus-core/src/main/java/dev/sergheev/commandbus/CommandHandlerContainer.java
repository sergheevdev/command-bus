package dev.sergheev.commandbus;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * A simple {@link CommandHandler} heterogeneous container implementation.
 *
 * <p>Unlike a regular container, this container allows to have different types for the keys and
 * also to hold values of many (i.e. heterogeneous) types. The type of token is used to guarantee
 * that the type of the key agrees with its value.</p>
 *
 * <p>This heterogeneous container implementation is a concretion of a more generic container,
 * because it restricts the types of the instances you can store by prohibiting the storage of
 * any value or key that is not assignable to {@link CommandHandler}.</p>
 *
 * @see <a href="https://www.informit.com/articles/article.aspx?p=1216151">Joshua Bloch's Effective Java - Item 1
 * @see <a href="https://www.informit.com/articles/article.aspx?p=1216151&seqNum=4">Joshua Bloch's Effective Java - Item 4
 * @see <a href="http://www.informit.com/articles/article.aspx?p=2861454&seqNum=8">Joshua Bloch's Effective Java - Item 33
 */
public class CommandHandlerContainer {

    /**
     * Creates a non thread-safe {@code CommandHandlerContainer} based on a {@link HashMap}.
     *
     * @return a non-thread safe {@code CommandHandlerContainer}
     */
    public static CommandHandlerContainer newInstance() {
        return new CommandHandlerContainer(HashMap::new);
    }

    /**
     * Creates a thread-safe {@code CommandHandlerContainer} based on a {@link ConcurrentHashMap}.
     *
     * @return a thread safe {@code CommandHandlerContainer}
     */
    public static CommandHandlerContainer newConcurrentInstance() {
        return new CommandHandlerContainer(ConcurrentHashMap::new);
    }

    private final Map<Class<?>, Object> handlerClassToInstance;

    /**
     * Suppress default constructor for non-instantiability.
     */
    private CommandHandlerContainer() {
        throw new AssertionError();
    }

    /**
     * Creates a {@code CommandHandlerContainer} based on the {@link Map} returned by the
     * provided map supplier {@link Supplier#get()} method.
     *
     * <p>The returned {@code CommandHandlerContainer} is thread-safe only if the {@link Map}
     * returned by the specified map supplier {@link Supplier#get()} method is thread-safe.
     * The behavior of the returned container is undefined if the supplied map is modified
     * externally.
     *
     * @param mapSupplier an empty map supplier used to obtain a map chosen implementation
     *
     * @throws NullPointerException if the map provided by {@code mapSupplier} is {@code null}
     * @throws IllegalArgumentException if the map provided by {@code mapSupplier} is not empty
     */
    protected CommandHandlerContainer(Supplier<Map<Class<?>, Object>> mapSupplier) {
        this.handlerClassToInstance = asEmptyMap(mapSupplier);
    }

    /**
     * Unwraps the concrete empty map implementation provided by the supplier.
     *
     * @param mapSupplier an empty map supplier used to obtain a map chosen implementation
     *
     * @throws NullPointerException if the map provided by {@code mapSupplier} is {@code null}
     * @throws IllegalArgumentException if the map provided by {@code mapSupplier} is not empty
     *
     * @return the map implementation provided by mapSupplier
     */
    private Map<Class<?>, Object> asEmptyMap(Supplier<Map<Class<?>, Object>> mapSupplier) {
        Map<Class<?>, Object> values = mapSupplier.get();
        final String message = "The provided supplier must be empty: mapSupplier";
        if(!values.isEmpty()) throw new IllegalArgumentException(message);
        return values;
    }

    /**
     * Stores the given instance in the container.
     *
     * @param type a type key
     * @param instance a instance value
     *
     * @throws NullPointerException if {@code type} is {@code null}
     * @throws NullPointerException if {@code instance} is {@code null}
     * @throws IllegalArgumentException if {@code type} and {@code instance} type are not equal
     * @throws IllegalArgumentException if {@code type} is not assignable to {@link CommandHandler}
     */
    public void register(Class<?> type, Object instance) {
        requireNonNull(type, "type");
        requireNonNull(instance, "instance");
        requireTypeMatchesInstanceType(type, instance);
        requireTypeAssignableToCommandHandler(type);
        handlerClassToInstance.put(type, instance);
    }

    /**
     * Removes the instance from the container.
     *
     * @param type a type key
     *
     * @throws NullPointerException if {@code type} is {@code null}
     * @throws IllegalArgumentException if {@code type} is not assignable to {@link CommandHandler}
     */
    public void unregister(Class<?> type) {
        requireNonNull(type, "type");
        requireTypeAssignableToCommandHandler(type);
        handlerClassToInstance.remove(type);
    }

    /**
     * Retrieves the stored {@link CommandHandler} concrete implementation from the container.
     *
     * @param type a type key
     *
     * @throws NullPointerException if {@code type} is {@code null}
     * @throws IllegalArgumentException if {@code type} is not assignable to {@link CommandHandler}
     *
     * @return a concrete implementation of {@link CommandHandler} of the key type.
     */
    public <T> T get(Class<T> type) {
        requireNonNull(type, "type");
        requireTypeAssignableToCommandHandler(type);
        return type.cast(handlerClassToInstance.get(type));
    }

    /**
     * @return true if the container is empty
     */
    public boolean isEmpty() {
        return handlerClassToInstance.isEmpty();
    }

    private void requireTypeMatchesInstanceType(Class<?> type, Object instance) {
        if(!type.equals(instance.getClass())) {
            final String message = String.format("Given type does not match instance type: %s != %s", type, instance.getClass());
            throw new IllegalArgumentException(message);
        }
    }

    private void requireTypeAssignableToCommandHandler(Class<?> type) {
        if(!CommandHandler.class.isAssignableFrom(type)){
            final String message = String.format("Given type must be assignable to CommandHandler: %s", type);
            throw new IllegalArgumentException(message);
        }
    }

}