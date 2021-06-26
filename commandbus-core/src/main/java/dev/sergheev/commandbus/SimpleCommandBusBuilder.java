package dev.sergheev.commandbus;

import dev.sergheev.commandbus.registry.CommandHandlerRegistry;
import dev.sergheev.commandbus.registry.CommandHandlerRegistryFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * A simple fluent API for the construction of a {@link SimpleCommandBus}.
 *
 * <p>This object is responsible for the instantiation of a {@link SimpleCommandBus},
 * it allows to easily construct the bus and specify custom properties that will
 * define the bus behavior at runtime (i.e. thread-safety).
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
     * Ensures the thread-safety of the constructed bus and its functioning in multithreaded environments.
     * @return the current builder
     */
    public SimpleCommandBusBuilder concurrent() {
        isConcurrent = true;
        return this;
    }

    /**
     * Registers a new handler that will contain behavior that is to be associated with a command.
     * @param type the type of the command handler object that is to be registered
     * @param instance the instance that matches the previously specified type
     * @throws IllegalArgumentException if the instance does not match the given type
     * @return the current builder
     */
    public SimpleCommandBusBuilder registerHandler(Class<? extends CommandHandler> type, Object instance) {
        if(!type.isInstance(instance)) throw new IllegalArgumentException("The given instance must match the type");
        classToInstance.put(type, instance);
        return this;
    }

    /**
     * Registers multiple new handlers that will contain behavior that is to be associated with their commands.
     * @param handlers a map containing the type-instance handler associations
     * @throws IllegalArgumentException if any instance in the given map does not match its type
     * @return the current builder
     */
    public SimpleCommandBusBuilder registerHandlers(Map<Class<? extends CommandHandler>, Object> handlers) {
        handlers.forEach(this::registerHandler);
        return this;
    }

    /**
     * Sets a manually managed or custom handler registry instance that is to be used in this bus.
     * @param customRegistry the custom command handler registry instance
     * @return the current builder
     */
    public SimpleCommandBusBuilder withRegistry(CommandHandlerRegistry customRegistry) {
        this.customRegistry = requireNonNull(customRegistry, "customRegistry");
        return this;
    }

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
