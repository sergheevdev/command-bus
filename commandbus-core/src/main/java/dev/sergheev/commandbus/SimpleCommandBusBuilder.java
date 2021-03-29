package dev.sergheev.commandbus;

import dev.sergheev.commandbus.registry.CommandHandlerRegistry;
import dev.sergheev.commandbus.registry.CommandHandlerRegistryFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple API for the construction of {@link SimpleCommandBus}.
 *
 * <p>The class is responsible for the instantiation of {@link SimpleCommandBus}
 * upon given preferences like {@link CommandHandler} pre-registration in the
 * container, or if the bus must be concurrent or not.</p>
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

    public SimpleCommandBusBuilder concurrent() {
        isConcurrent = true;
        return this;
    }

    public SimpleCommandBusBuilder registerHandler(Class<? extends CommandHandler> type, Object instance) {
        classToInstance.put(type, instance);
        return this;
    }

    public SimpleCommandBusBuilder registerHandlers(Map<Class<? extends CommandHandler>, Object> handlers) {
        handlers.forEach(classToInstance::put);
        return this;
    }

    public SimpleCommandBusBuilder withRegistry(CommandHandlerRegistry customRegistry) {
        this.customRegistry = customRegistry;
        return this;
    }

    public SimpleCommandBus build() {
        CommandHandlerRegistry commandHandlerRegistry;

        if(Objects.isNull(customRegistry)) {
            if(isConcurrent) {
                commandHandlerRegistry = CommandHandlerRegistryFactory.newConcurrentRegistry();
            } else {
                commandHandlerRegistry = CommandHandlerRegistryFactory.newRegistry();
            }
        } else {
            commandHandlerRegistry = customRegistry;
        }

        classToInstance.forEach(commandHandlerRegistry::registerHandler);
        CommandHandlerFinder commandHandlerFinder = new SimpleCommandHandlerFinder(commandHandlerRegistry);

        return new SimpleCommandBus(commandHandlerFinder);
    }

}
