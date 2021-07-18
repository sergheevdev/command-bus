package dev.sergheev.commandbus;

import dev.sergheev.commandbus.registry.CommandHandlerRegistry;

import static java.util.Objects.requireNonNull;

/**
 * A simple implementation for a {@link CommandHandlerFinder}.
 *
 * <p>This object is responsible for the retrieval of different {@link CommandHandler}
 * implementations based on a given command name, the returned implementation will
 * be able to handle the given command that associated to the provided command name.
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
    public SimpleCommandHandlerFinder(CommandHandlerRegistry commandHandlerRegistry) throws NullPointerException {
        requireNonNull(commandHandlerRegistry, "commandHandlerRegistry must not be null");
        this.commandHandlerRegistry = commandHandlerRegistry;
    }

    /**
     * Returns a concrete handler implementation that is able to process the command associated to the given command name.
     * @param commandName the name of the command whose handler is to be found (fully qualified {@link Command} class name)
     * @param <C> the type of the concrete command
     * @param <R> the type of the command computation result
     * @throws NullPointerException if {@code commandName} is {@code null}
     * @throws IllegalArgumentException if {@code commandName} is empty (the length of the string is zero)
     * @return a concrete {@link CommandHandler} implementation able to handle the given command
     */
    public <C extends Command, R> CommandHandler<C, R> findHandlerFor(String commandName) throws NullPointerException, IllegalArgumentException {
        requireNonNull(commandName, "commandName must not be null");
        if(commandName.isEmpty()) throw new IllegalArgumentException("commandName must not be empty");
        return commandHandlerRegistry.getHandlerFor(commandName);
    }

}
