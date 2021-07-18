package dev.sergheev.commandbus;

import static java.util.Objects.requireNonNull;

/**
 * A simple implementation for a {@link CommandBus}.
 *
 * <p>This object is responsible for taking in a {@link Command}, associating
 * it to its handler, and using that handler to perform the desired action,
 * finally returning the command execution result.
 */
public class SimpleCommandBus implements CommandBus {

    /**
     * Stores all the command-handler associations.
     */
    private final CommandHandlerFinder commandHandlerFinder;

    /**
     * Constructs a new {@link SimpleCommandBus} instance.
     * @throws NullPointerException if the {@code commandHandlerFinder} is {@code null}
     */
    public SimpleCommandBus(CommandHandlerFinder commandHandlerFinder) throws NullPointerException {
        requireNonNull(commandHandlerFinder, "commandHandlerFinder must not be null");
        this.commandHandlerFinder = commandHandlerFinder;
    }

    /**
     * Returns the resulting object from processing the given command.
     * @param command the command that is to be processed
     * @param <R> the type of the returned result
     * @throws NullPointerException if the given {@code command} is {@code null}
     * @return the resulting object from processing the given command
     */
    @Override
    public <R> R execute(Command command) throws NullPointerException {
        requireNonNull(command, "command must not be null");
        CommandHandler<Command, R> handler = commandHandlerFinder.findHandlerFor(command.getClass().getName());
        return handler.handle(command);
    }

}
