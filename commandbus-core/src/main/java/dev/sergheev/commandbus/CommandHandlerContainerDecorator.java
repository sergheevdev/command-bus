package dev.sergheev.commandbus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * A simple decorator implementation for {@link CommandHandlerContainer}.
 *
 * <p>The purpose of this decorator implementation is to add another layer of functionality to
 * {@link CommandHandlerContainer}, that is because we are dealing with a ternary relationship,
 * first we need to associate a command name to a concrete class (responsibility of this decorator),
 * and then given that class associate it to a concrete handler implementation.</p>
 *
 * @see <a href="https://www.informit.com/articles/article.aspx?p=1216151">Joshua Bloch's Effective Java - Item 1
 * @see <a href="https://www.informit.com/articles/article.aspx?p=1216151&seqNum=4">Joshua Bloch's Effective Java - Item 4
 * @see <a href="https://www.oreilly.com/library/view/design-patterns-elements/0201633612/">Design Patterns: Elements of Reusable Object-Oriented Software - Structural Patterns - Decorator
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CommandHandlerContainerDecorator {

    /**
     * Creates a non thread-safe {@code CommandHandlerContainerDecorator} based on a {@link HashMap}.
     *
     * @return a non-thread safe {@code CommandHandlerContainerDecorator}
     */
    public static CommandHandlerContainerDecorator newInstance() {
        return new CommandHandlerContainerDecorator(CommandHandlerContainer.newInstance(), HashMap::new);
    }

    /**
     * Creates a thread-safe {@code CommandHandlerContainerDecorator} based on a {@link ConcurrentHashMap}.
     *
     * @return a thread safe {@code CommandHandlerContainerDecorator}
     */
    public static CommandHandlerContainerDecorator newConcurrentInstance() {
        return new CommandHandlerContainerDecorator(CommandHandlerContainer.newConcurrentInstance(), ConcurrentHashMap::new);
    }

    private final Lock decoratorLock;

    private final CommandHandlerContainer commandHandlerContainer;

    private final Map<String, Class<? extends CommandHandler>> commandNameToHandlerClass;

    private final CommandHandlerMappingExtractorDecorator commandNameFromMappingExtractor;

    /**
     * Suppress default constructor for non-instantiability.
     */
    private CommandHandlerContainerDecorator() {
        throw new AssertionError();
    }

    /**
     * Creates a {@code CommandHandlerContainer} based on the {@link Map} returned by the provided
     * map supplier {@link Supplier#get()} method.
     *
     * <p>The returned {@code CommandHandlerContainerDecorator} is thread-safe only if the {@link Map}
     * returned by the specified map supplier {@link Supplier#get()} method is thread-safe. The
     * behavior of the returned decorator is undefined if the supplied map is modified externally or
     * if the supplier which was given to the decorator is thread-safe and the container supplier is
     * non thread-safe, and viceversa.
     *
     * @param commandHandlerContainer an empty {@link CommandHandlerContainer}
     * @param mapSupplier an empty map supplier used to obtain a map chosen implementation
     *
     * @throws NullPointerException if the map provided by {@code mapSupplier} is {@code null}
     */
    protected CommandHandlerContainerDecorator(CommandHandlerContainer commandHandlerContainer,
                                               Supplier<Map<String, Class<? extends CommandHandler>>> mapSupplier) {
        this.decoratorLock = new ReentrantLock();
        this.commandHandlerContainer = requireNonNull(commandHandlerContainer);
        this.commandNameToHandlerClass = asEmptyMap(mapSupplier);
        this.commandNameFromMappingExtractor = new CommandHandlerMappingExtractorDecorator();
    }

    /**
     * Unwraps the concrete empty map implementation provided by the supplier.
     *
     * @param mapSupplier an empty map supplier used to obtain a map chosen implementation
     *
     * @throws NullPointerException if the map provided by {@code mapSupplier} is {@code null}
     * @throws IllegalArgumentException if the map provided by {@code mapSupplier} is not empty
     *
     * @return the map implementation provided by {@code mapSupplier}
     */
    private Map<String, Class<? extends CommandHandler>> asEmptyMap(Supplier<Map<String, Class<? extends CommandHandler>>> mapSupplier) {
        Map<String, Class<? extends CommandHandler>> values = mapSupplier.get();
        final String message = "The provided supplier must be empty: mapSupplier";
        if(!values.isEmpty()) throw new IllegalArgumentException(message);
        return values;
    }

    /**
     * Finds a concrete {@link CommandHandler} implementation associated to the given command name.
     *
     * @throws NullPointerException if commandName is {@code null}
     *
     * @return the {@link CommandHandler} concrete instance associated to the given command name.
     */
    public <C extends Command, R> CommandHandler<C, R> findHandlerFor(String commandName) {
        requireNonNull(commandName, "commandName");
        decoratorLock.lock();
        try {
            Class<? extends CommandHandler> type = commandNameToHandlerClass.get(commandName);
            return commandHandlerContainer.get(type);
        } finally {
            decoratorLock.unlock();
        }
    }

    /**
     * Stores the instance in the wrapped container and saves the command name to handler relationship.
     *
     * @param type a type key
     * @param instance a instance value
     *
     * @throws NullPointerException if the type is {@code null}
     */
    public void registerHandler(Class<? extends CommandHandler> type, Object instance) {
        requireNonNull(type, "type");
        decoratorLock.lock();
        List<String> commandNames = commandNameFromMappingExtractor.extractCommandNamesFor(type);
        commandNames.forEach(name -> commandNameToHandlerClass.put(name, type));
        try {
            commandHandlerContainer.register(type, instance);
        } finally {
            decoratorLock.unlock();
        }
    }

    /**
     * Removes the instance from the wrapped container and from the command name to handler relationship.
     *
     * @param type a type key
     *
     * @throws NullPointerException if {@code type} is {@code null}
     */
    public void unregisterHandler(Class<? extends CommandHandler> type) {
        requireNonNull(type, "type");
        decoratorLock.lock();
        List<String> commandNames = commandNameFromMappingExtractor.extractCommandNamesFor(type);
        commandNames.forEach(commandNameToHandlerClass::remove);
        try {
            commandHandlerContainer.unregister(type);
        } finally {
            decoratorLock.unlock();
        }
    }

    /**
     * Retrieves the stored {@link CommandHandler} instance from the wrapped container.
     *
     * @param type the given type key
     *
     * @return a concrete implementation of {@link CommandHandler} of the key type.
     */
    public <T> T getHandler(Class<T> type) {
        return commandHandlerContainer.get(type);
    }

    /**
     * @return true if the wrapped container is empty
     */
    public boolean isContainerEmpty() {
        return commandHandlerContainer.isEmpty();
    }

}
