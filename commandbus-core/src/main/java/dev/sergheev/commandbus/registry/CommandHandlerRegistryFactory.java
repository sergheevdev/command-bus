package dev.sergheev.commandbus.registry;

import dev.sergheev.commandbus.container.SimpleContainer;

/**
 * Simple class with static creation methods for {@link CommandHandlerRegistry}.
 */
public class CommandHandlerRegistryFactory {

    public static SimpleCommandHandlerRegistry newRegistry() {
        return new SimpleCommandHandlerRegistry(SimpleContainer.newInstance());
    }

    public static ConcurrentCommandHandlerRegistry newConcurrentRegistry() {
        return new ConcurrentCommandHandlerRegistry(SimpleContainer.newInstance());
    }

}
