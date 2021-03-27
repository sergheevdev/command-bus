package dev.sergheev.commandbus;

/**
 * An intermediate router that delivers the provided command to its
 * respective {@link CommandHandler}, returning an execution answer.
 */
public interface CommandBus {

    /**
     * Returns an answer based on the execution state of the command.
     *
     * @param command the concrete command
     *
     * @return the execution result
     */
    <R> R execute(Command command);

}
