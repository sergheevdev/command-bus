package dev.sergheev.commandbus;

/**
 * The {@code CommandHandlerFinder} responsibility is to map the
 * provided command name to its correspondent command handler.
 */
public interface CommandHandlerFinder {

    <C extends Command, R> CommandHandler<C, R> findHandlerFor(String commandName);

}
