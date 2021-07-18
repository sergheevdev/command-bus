package dev.sergheev.commandbus;

/**
 * The {@code CommandHandlerFinder} responsibility is to map the
 * provided command name to its correspondent command handler.
 */
public interface CommandHandlerFinder {

    /**
     * Returns a concrete handler implementation that is able to process the command associated to the given command name.
     * @param commandName the name of the command whose handler is to be found (fully qualified {@link Command} class name)
     * @param <C> the type of the concrete command
     * @param <R> the type of the command computation result
     * @throws NullPointerException if {@code commandName} is {@code null}
     * @throws IllegalArgumentException if {@code commandName} is empty (the length of the string is zero)
     * @return a concrete {@link CommandHandler} implementation able to handle the given command
     */
    <C extends Command, R> CommandHandler<C, R> findHandlerFor(String commandName) throws NullPointerException, IllegalArgumentException;

}
