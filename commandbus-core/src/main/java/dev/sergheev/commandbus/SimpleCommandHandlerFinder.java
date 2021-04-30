package dev.sergheev.commandbus;

import dev.sergheev.commandbus.registry.CommandHandlerRegistry;

/**
 * An object responsible of returning different {@link CommandHandler}
 * implementations, the returned implementation will be the one that
 * is able to handle the provided command name.
 */
public class SimpleCommandHandlerFinder implements CommandHandlerFinder {

    private final CommandHandlerRegistry commandHandlerRegistry;

    public SimpleCommandHandlerFinder(CommandHandlerRegistry commandHandlerRegistry) {
        this.commandHandlerRegistry = commandHandlerRegistry;
    }

    /**
     * Finds the {@link CommandHandler} implementation that is able to handle
     * the given command name.
     *
     * @param commandName the name of the command whose handler is to be found
     *
     * @return a {@link CommandHandler} implementation that is able to handle
     *         the given command name
     */
    public <C extends Command, R> CommandHandler<C, R> findHandlerFor(String commandName) {
        return commandHandlerRegistry.getHandlerFor(commandName);
    }

}
