package dev.sergheev.commandbus;

import static java.util.Objects.requireNonNull;

/**
 * A simple implementation for a {@link CommandBus}.
 *
 * This object is responsible of taking in a {@link Command}, associating
 * it to its handler, and using that handler to perform the desired action
 * finally returning the command execution result.
 */
public class SimpleCommandBus implements CommandBus {

    private final CommandHandlerFinder handlerFinder;

    public SimpleCommandBus(CommandHandlerFinder commandHandlerFinder) {
        this.handlerFinder = requireNonNull(commandHandlerFinder, "commandHandlerFinder");
    }

    /**
     * Returns the result from processing the given command.
     * @param command the command that is to be processed
     * @param <R> the type of the returned result
     * @return the result from processing the given command
     */
    @Override
    public <R> R execute(Command command) {
        requireNonNull(command, "command");
        CommandHandler<Command, R> handler = handlerFinder.findHandlerFor(command.getClass().getName());
        return handler.handle(command);
    }

}
