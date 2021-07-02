package dev.sergheev.commandbus;

import dev.sergheev.commandbus.registry.CommandHandlerRegistry;

import static java.util.Objects.requireNonNull;

/**
 * A simple implementation for a {@link CommandHandlerFinder}.
 *
 * This object is responsible for the retrieval of different {@link CommandHandler}
 * implementations based on a given command name, the returned implementation will
 * be able to handle the command that associated to the provided command name.
 */
public class SimpleCommandHandlerFinder implements CommandHandlerFinder {

    private final CommandHandlerRegistry commandHandlerRegistry;

    public SimpleCommandHandlerFinder(CommandHandlerRegistry commandHandlerRegistry) {
        this.commandHandlerRegistry = requireNonNull(commandHandlerRegistry, "commandHandlerRegistry");
    }

    /**
     * Returns a concrete {@link CommandHandler} implementation that is able to process the given command.
     * @param commandName the name of the command whose handler is to be found
     * @param <C> the type of the concrete command
     * @param <R> the type of the command computation result
     * @return a concrete {@link CommandHandler} implementation able to handle the given command
     */
    public <C extends Command, R> CommandHandler<C, R> findHandlerFor(String commandName) {
        requireNonNull(commandName, "commandName");
        return commandHandlerRegistry.getHandlerFor(commandName);
    }

}
