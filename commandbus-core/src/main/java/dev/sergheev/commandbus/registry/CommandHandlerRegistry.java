package dev.sergheev.commandbus.registry;

import dev.sergheev.commandbus.Command;
import dev.sergheev.commandbus.CommandHandler;

/**
 * An object which represents a registry of {@link CommandHandler} instances.
 */
@SuppressWarnings({ "rawtypes" })
public interface CommandHandlerRegistry {

    /**
     * Stores the specified instance with the type in the registry.
     *
     * @param type type with which the instance is to be registered
     * @param instance instance to be registered with the specified key
     * @param <T> the type of the value
     *
     * @return the current instance registered with {@code type}
     */
    <T extends CommandHandler> T registerHandler(Class<T> type, Object instance);

    /**
     * Removes the instance associated with the given {@code type}.
     *
     * @param type type whose instance of to be removed from the registry
     * @param <T> the type of the value
     *
     * @return the previous value associated with {@code type}, or
     *         {@code null} if there was no instance registered for
     *         {@code key}.
     */
    <T extends CommandHandler> T unregisterHandler(Class<T> type);

    /**
     * Returns the instance to which the specified key is registered.
     *
     * @param type type whose registered instance is to be returned
     * @param <T> the type of the value
     *
     * @return the current value registered with {@code type}, or
     *         {@code null} if there was no registered for {@code type}
     */
    <T extends CommandHandler> T getHandler(Class<T> type);

    /**
     * Returns the instance which is able to handle the given command name.
     *
     * @param commandName command class name
     * @param <C> type of the command
     * @param <R> type of the handler's response to that given command execution
     *
     * @return the instance which is able to handle the given command name
     */
    <C extends Command, R> CommandHandler<C, R> getHandlerFor(String commandName);

    /**
     * Returns {@code true} if this registry has any registration with the specified type.
     *
     * @param type type whose presence in this registry is to be tested
     *
     * @return {@code true} if this registry has any registration with the specified type
     */
    boolean containsHandler(Class<? extends CommandHandler> type);

    /**
     * Removes all the registered handlers from the registry.
     */
    void clearRegistry();

    /**
     * Returns {@code true} if this registry has no stored instances.
     *
     * @return {@code true} if this registry has no stored instances.
     */
    boolean isRegistryEmpty();

    /**
     * Returns the amount of type-instance currently registered.
     *
     * @return the amount of type-instance currently registered.
     */
    int registrySize();

}
