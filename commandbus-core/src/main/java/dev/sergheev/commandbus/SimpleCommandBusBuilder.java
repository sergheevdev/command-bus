package dev.sergheev.commandbus;

import dev.sergheev.commandbus.registry.CommandHandlerRegistry;
import dev.sergheev.commandbus.registry.CommandHandlerRegistryFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * A simple fluent API builder for the construction of a {@link SimpleCommandBus}.
 *
 * <p>This object is responsible to provide the clients a simple API for the construction
 * of a {@link SimpleCommandBus}. It allows to easily construct the bus and specify custom
 * properties that will define the bus behavior at runtime (i.e. thread-safety).
 */
@SuppressWarnings({ "rawtypes" })
public class SimpleCommandBusBuilder {

    public static SimpleCommandBusBuilder create() {
        return new SimpleCommandBusBuilder();
    }

    private boolean isConcurrent;
    private CommandHandlerRegistry customRegistry;
    private final Map<Class<? extends CommandHandler>, Object> classToInstance;

    public SimpleCommandBusBuilder() {
        this.isConcurrent = false;
        this.customRegistry = null;
        this.classToInstance = new HashMap<>();
    }

    /**
     * Ensures the thread-safety of the bus and its functioning in multithreaded environments.
     * @return the current builder
     */
    public SimpleCommandBusBuilder concurrent() {
        isConcurrent = true;
        return this;
    }

    /**
     * Associates all the map entries types with the concrete instances for the bus being built.
     * @param handlers the map containing all the type-instance handler associations
     * @throws NullPointerException if any {@code type} or {@code instance} is null at some entry
     * @throws IllegalArgumentException if any {@code instance} at some entry is not of the specified {@code type}
     * @return the current builder
     */
    public SimpleCommandBusBuilder registerHandlers(Map<Class<? extends CommandHandler>, Object> handlers) {
        handlers.forEach(this::registerHandler);
        return this;
    }

    /**
     * Associates the specified type with the concrete instance for the bus being built.
     * @param type the type that represents the handler class
     * @param instance the instance that represents the concrete instance
     * @throws NullPointerException if the {@code type} or the {@code instance} are null
     * @throws IllegalArgumentException if the {@code instance} is not of the specified {@code type}
     * @return the current builder
     */
    public SimpleCommandBusBuilder registerHandler(Class<? extends CommandHandler> type, Object instance) {
        requireNonNull(type, "type");
        requireNonNull(instance, "instance");
        if(!type.isInstance(instance)) throw new IllegalArgumentException("The given instance must match the type");
        classToInstance.put(type, instance);
        return this;
    }

    /**
     * Establishes the usage of a manually managed or custom registry instance for the bus being built.
     * @param customRegistry the custom registry that is to be used
     * @return the current builder
     */
    public SimpleCommandBusBuilder withRegistry(CommandHandlerRegistry customRegistry) {
        this.customRegistry = requireNonNull(customRegistry, "customRegistry");
        return this;
    }

    /**
     * Returns a new {@link SimpleCommandBus} configured accordingly to this builder's set properties.
     * @return a new {@link SimpleCommandBus} instance
     */
    public SimpleCommandBus build() {
        CommandHandlerRegistry handlerRegistry;
        boolean hasCustomRegistry = !Objects.isNull(customRegistry);
        if(hasCustomRegistry) {
            handlerRegistry = customRegistry;
        } else if(isConcurrent) {
            handlerRegistry = CommandHandlerRegistryFactory.newConcurrentRegistry();
        } else {
            handlerRegistry = CommandHandlerRegistryFactory.newRegistry();
        }
        classToInstance.forEach(handlerRegistry::registerHandler);
        CommandHandlerFinder commandHandlerFinder = new SimpleCommandHandlerFinder(handlerRegistry);
        return new SimpleCommandBus(commandHandlerFinder);
    }

}
