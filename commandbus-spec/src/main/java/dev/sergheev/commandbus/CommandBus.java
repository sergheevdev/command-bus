package dev.sergheev.commandbus;

/**
 * An intermediate router that delivers the provided command to its
 * respective {@link CommandHandler}, returning an execution answer.
 */
public interface CommandBus {

    /**
     * Returns the resulting object from processing the given command.
     * @param command the command that is to be processed
     * @param <R> the type of the returned result
     * @throws NullPointerException if the given {@code command} is {@code null}
     * @return the resulting object from processing the given command
     */
    <R> R execute(Command command) throws NullPointerException;

}
