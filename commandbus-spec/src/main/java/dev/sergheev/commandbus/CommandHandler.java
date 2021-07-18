package dev.sergheev.commandbus;

/**
 * The {@code CommandHandler} responsibility is to execute the
 * appropriate behavior when receiving a specific {@link Command}.
 */
public interface CommandHandler<C extends Command, R> {

    /**
     * Receives, processes and returns the command execution result
     * @param command the concrete command that is to be processed
     * @return the command execution result
     */
    R handle(C command);

}