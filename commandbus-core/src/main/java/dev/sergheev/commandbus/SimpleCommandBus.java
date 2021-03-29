package dev.sergheev.commandbus;

/**
 * A simple {@link CommandBus} implementation.
 */
public class SimpleCommandBus implements CommandBus {

    private final CommandHandlerFinder handlerFinder;

    public SimpleCommandBus(CommandHandlerFinder commandHandlerFinder) {
        this.handlerFinder = commandHandlerFinder;
    }

    @Override
    public <R> R execute(Command command) {
        CommandHandler<Command, R> handler = handlerFinder.findHandlerFor(command.getClass().getName());
        return handler.handle(command);
    }

}
