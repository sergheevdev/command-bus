package dev.sergheev.commandbus.registry;

import dev.sergheev.commandbus.container.SimpleContainer;

/**
 * Simple factory with static creation methods for {@link CommandHandlerRegistry}.
 */
public class CommandHandlerRegistryFactory {

    /**
     * Creates a non thread-safe {@link SimpleCommandHandlerRegistry}.
     * @return a non thread-safe {@link SimpleCommandHandlerRegistry}.
     */
    public static SimpleCommandHandlerRegistry newRegistry() {
        return new SimpleCommandHandlerRegistry(SimpleContainer.newInstance());
    }

    /**
     * Creates a thread-safe {@link SimpleCommandHandlerRegistry}.
     * @return a thread-safe {@link SimpleCommandHandlerRegistry}.
     */
    public static ConcurrentCommandHandlerRegistry newConcurrentRegistry() {
        return new ConcurrentCommandHandlerRegistry(SimpleContainer.newInstance());
    }

}
