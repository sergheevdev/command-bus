package dev.sergheev.commandbus.registry;

/**
 * Simple class with static creation methods for {@link CommandHandlerRegistry}.
 */
public class CommandHandlerRegistryFactory {

    public static SimpleCommandHandlerRegistry newRegistry() {
        return SimpleCommandHandlerRegistry.newInstance();
    }

    public static ConcurrentCommandHandlerRegistry newConcurrentRegistry() {
        return ConcurrentCommandHandlerRegistry.newInstance();
    }

}
