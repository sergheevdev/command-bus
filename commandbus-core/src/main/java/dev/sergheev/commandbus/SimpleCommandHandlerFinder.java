package dev.sergheev.commandbus;

import dev.sergheev.commandbus.registry.CommandHandlerRegistry;

/**
 * A factory responsible of returning different {@link CommandHandler}
 * implementations, each one depending on the given command name.
 */
public class SimpleCommandHandlerFinder implements CommandHandlerFinder {

    private final CommandHandlerRegistry commandHandlerRegistry;

    public SimpleCommandHandlerFinder(CommandHandlerRegistry commandHandlerRegistry) {
        this.commandHandlerRegistry = commandHandlerRegistry;
    }

    /**
     * Finds the {@link CommandHandler} implementation associated to the command name.
     *
     * @param commandName the name of the command
     *
     * @return a {@link CommandHandler} implementation associated to the given command name
     */
    public <C extends Command, R> CommandHandler<C, R> findHandlerFor(String commandName) {
        return commandHandlerRegistry.getHandlerFor(commandName);
    }

}
