package dev.sergheev.commandbus;

import dev.sergheev.commandbus.registry.CommandHandlerRegistry;
import dev.sergheev.commandbus.registry.CommandHandlerRegistryFactory;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

/**
 * A simple fluent API builder for the construction of a {@link SimpleCommandBus}.
 *
 * <p>This object is responsible for providing the clients a simple API for the
 * construction of a {@link SimpleCommandBus}, by using this builder we prevent
 * manual assembly confusion, because we provide a simple way to construct a
 * complex object.
 *
 * <p>Apart from allowing us to easily construct the bus (in a simple way), it
 * also allows us to specify custom properties that will define the bus behavior
 * at runtime (i.e. thread-safety).
 */
@SuppressWarnings({ "rawtypes" })
public class SimpleCommandBusBuilder {

    /**
     * A static factory method for the creation of a new {@code SimpleCommandBusBuilder} instance.
     * @return a new {@link SimpleCommandBusBuilder} instance
     */
    public static SimpleCommandBusBuilder create() {
        return new SimpleCommandBusBuilder();
    }

    /**
     * Indicates if the bus being built must be thread-safe (concurrent) or not.
     */
    private boolean isConcurrent;

    /**
     * Stores a custom or client-managed registry that is to be provided by the client.
     */
    private CommandHandlerRegistry customRegistry;

    /**
     * Contains all the handler class to instance associations that are to be stored in the registry.
     */
    private final Map<Class<? extends CommandHandler>, Object> classToInstance;

    public SimpleCommandBusBuilder() {
        this.isConcurrent = false;
        this.customRegistry = null;
        this.classToInstance = new ConcurrentHashMap<>();
    }

    /**
     * Ensures the thread-safety of the bus being built and its functioning in multi-threaded environments.
     * @return the current {@link SimpleCommandBusBuilder} instance
     */
    public SimpleCommandBusBuilder concurrent() {
        isConcurrent = true;
        return this;
    }

    /**
     * Registers all the type-instance handler associations for the bus being built.
     * @param classToHandler the map containing all the type-instance handler associations
     * @throws NullPointerException if any {@code type} or {@code instance} mapping is {@code null}
     * @throws IllegalArgumentException if the {@code handlers} map is {@code null}
     * @throws IllegalArgumentException if any {@code instance} is not of the specified {@code type}
     * @return the current {@link SimpleCommandBusBuilder} instance
     */
    public SimpleCommandBusBuilder registerHandlers(Map<Class<? extends CommandHandler>, Object> classToHandler) throws NullPointerException, IllegalArgumentException {
        requireNonNull(classToHandler, "classToHandler must not be null");
        classToHandler.forEach(this::registerHandler);
        return this;
    }

    /**
     * Registers a type-instance handler association for the bus being built.
     * @param type the type of the handler class
     * @param instance the instance of the previously mentioned type
     * @throws NullPointerException if the {@code type} or the {@code instance} are {@code null}
     * @throws IllegalArgumentException if the {@code instance} is not of the specified {@code type}
     * @return the current {@link SimpleCommandBusBuilder} instance
     */
    public SimpleCommandBusBuilder registerHandler(Class<? extends CommandHandler> type, Object instance) throws NullPointerException, IllegalArgumentException {
        requireNonNull(type, "type must not be null");
        requireNonNull(instance, "instance must not be null");
        if(!type.isInstance(instance)) throw new IllegalArgumentException("The given instance must match the type");
        classToInstance.put(type, instance);
        return this;
    }

    /**
     * Use a client-managed or custom-provided registry instance for the bus being built.
     * It is useful to provide a client self-managed registry when commands and handlers
     * will be modified at runtime, to prevent a {@link ConcurrentModificationException}.
     * @param customRegistry the custom registry that is to be used
     * @throws NullPointerException if the given {@code customRegistry} is {@code null}
     * @return the current {@link SimpleCommandBusBuilder} instance
     */
    public SimpleCommandBusBuilder withRegistry(CommandHandlerRegistry customRegistry) throws NullPointerException {
        requireNonNull(customRegistry, "customRegistry must not be null");
        this.customRegistry = customRegistry;
        return this;
    }

    /**
     * Constructs a new {@link SimpleCommandBus} instance configured accordingly. If a custom or
     * client-managed {@link CommandHandlerRegistry} is not specified, a default non thread-safe
     * implementation of the registry will be used instead.
     * @return a new {@link SimpleCommandBus} instance configured accordingly.
     */
    public SimpleCommandBus build() {
        final CommandHandlerRegistry handlerRegistry;
        final boolean hasCustomRegistry = !Objects.isNull(customRegistry);
        if(hasCustomRegistry) {
            handlerRegistry = customRegistry;
        } else if(isConcurrent) {
            handlerRegistry = CommandHandlerRegistryFactory.newConcurrentRegistry();
        } else {
            handlerRegistry = CommandHandlerRegistryFactory.newRegistry();
        }
        classToInstance.forEach(handlerRegistry::registerHandler);
        final CommandHandlerFinder commandHandlerFinder = new SimpleCommandHandlerFinder(handlerRegistry);
        return new SimpleCommandBus(commandHandlerFinder);
    }

}
