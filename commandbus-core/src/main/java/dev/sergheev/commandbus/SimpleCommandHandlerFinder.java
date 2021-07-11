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

    /**
     * Used for the retrieval of stored handler instances.
     */
    private final CommandHandlerRegistry commandHandlerRegistry;

    /**
     * Constructs a new {@link SimpleCommandHandlerFinder} instance.
     * @throws NullPointerException if the {@code commandHandlerRegistry} is {@code null}
     */
    public SimpleCommandHandlerFinder(CommandHandlerRegistry commandHandlerRegistry) {
        requireNonNull(commandHandlerRegistry, "commandHandlerRegistry");
        this.commandHandlerRegistry = commandHandlerRegistry;
    }

    /**
     * Returns a concrete handler implementation that is able to process the given command.
     * @param commandName the name of the command whose handler is to be found
     * @param <C> the type of the concrete command
     * @param <R> the type of the command computation result
     * @throws NullPointerException if {@code commandName} is {@code null}
     * @throws IllegalArgumentException if {@code commandName} is empty
     * @return a concrete {@link CommandHandler} implementation able to handle the given command
     */
    public <C extends Command, R> CommandHandler<C, R> findHandlerFor(String commandName) {
        requireNonNull(commandName, "commandName must not be null");
        if(commandName.isEmpty()) throw new IllegalArgumentException("commandName must not be empty");
        return commandHandlerRegistry.getHandlerFor(commandName);
    }

}
