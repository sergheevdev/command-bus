package dev.sergheev.commandbus;

import java.util.HashMap;
import java.util.Map;

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
    private final Map<Class<? extends CommandHandler>, Object> classToInstance;

    public SimpleCommandBusBuilder() {
        this.isConcurrent = false;
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

    public SimpleCommandBus build() {
        CommandHandlerContainerDecorator commandHandlerContainerDecorator;
        if(isConcurrent) {
            commandHandlerContainerDecorator = CommandHandlerContainerDecorator.newConcurrentInstance();
        } else {
            commandHandlerContainerDecorator = CommandHandlerContainerDecorator.newInstance();
        }
        classToInstance.forEach(commandHandlerContainerDecorator::registerHandler);
        CommandHandlerFinder commandHandlerFinder = new SimpleCommandHandlerFinder(commandHandlerContainerDecorator);

        return new SimpleCommandBus(commandHandlerFinder);
    }

}
