package dev.sergheev.commandbus;

/**
 * The {@code CommandHandler} responsibility is to execute the
 * appropriate behavior when receiving a specific {@link Command}.
 */
public interface CommandHandler<C extends Command, R> {

    /**
     * Receives, digests and returns an answer about the given command execution.
     *
     * @param command the concrete command
     *
     * @return the execution result
     */
    R handle(C command);

}