package dev.sergheev.commandbus;

/**
 * A factory responsible of returning different {@link CommandHandler}
 * implementations, each one depending on the given command name.
 */
public class SimpleCommandHandlerFinder implements CommandHandlerFinder {

    private final CommandHandlerContainerDecorator handlerStoreWrapper;

    public SimpleCommandHandlerFinder(CommandHandlerContainerDecorator handlerStoreWrapper) {
        this.handlerStoreWrapper = handlerStoreWrapper;
    }

    /**
     * Finds the {@link CommandHandler} implementation associated to the command name.
     *
     * @param commandName the name of the command
     *
     * @return a {@link CommandHandler} implementation associated to the given command name
     */
    public <C extends Command, R> CommandHandler<C, R> findHandlerFor(String commandName) {
        return handlerStoreWrapper.findHandlerFor(commandName);
    }

}
