package dev.sergheev.commandbus;

/**
 * A simple {@link CommandBus} implementation.
 *
 * <p>Responsible for finding the handler implementation associated
 * to the given command, and then making the handler process the
 * given command.</p>
 */
public class SimpleCommandBus implements CommandBus {

    private final CommandHandlerFinder handlerFinder;

    public SimpleCommandBus(CommandHandlerFinder commandHandlerFinder) {
        this.handlerFinder = commandHandlerFinder;
    }

    /**
     * Executes the given command.
     *
     * @param command the concrete command to be executed
     * @param <R> response type from the handler
     *
     * @return a response object that returns the handler
     */
    @Override
    public <R> R execute(Command command) {
        CommandHandler<Command, R> handler = handlerFinder.findHandlerFor(command.getClass().getName());
        return handler.handle(command);
    }

}
