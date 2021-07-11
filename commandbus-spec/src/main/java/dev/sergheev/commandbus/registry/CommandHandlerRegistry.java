package dev.sergheev.commandbus.registry;

import dev.sergheev.commandbus.Command;
import dev.sergheev.commandbus.CommandHandler;

/**
 * An interface that represents a registry of {@link CommandHandler} instances.
 *
 * A {@code CommandHandlerRegistry} is simply a container of {@link CommandHandler}
 * instances, it stores command handlers {@code type} to {@code instance} mappings.
 */
@SuppressWarnings({ "rawtypes" })
public interface CommandHandlerRegistry {

    /**
     * Stores the specified type-instance relationship in this registry.
     * @param type type with which the instance is to be associated
     * @param instance instance to be associated with the specified type
     * @param <T> the type of the instance
     * @return the newly registered instance associated to {@code type}
     */
    <T extends CommandHandler> T registerHandler(Class<T> type, Object instance);

    /**
     * Removes the instance to which the type is associated from this registry.
     * @param type type whose instance of to be removed from this registry
     * @param <T> the type of the value
     * @return the previous instance associated with {@code type}, or {@code null}
     *         if there was no instance associated to {@code key}.
     */
    <T extends CommandHandler> T unregisterHandler(Class<T> type);

    /**
     * Returns the instance to which the specified key is associated.
     * @param type type whose associated instance is to be returned
     * @param <T> the type of the value
     * @return the current instance associated to {@code type}, or {@code null}
     *         if there was no association for {@code type}
     */
    <T extends CommandHandler> T getHandler(Class<T> type);

    /**
     * Returns the instance which is able to handle a given command name.
     * @param commandName command class name
     * @param <C> type of the command
     * @param <R> type of the handler's response
     * @return the instance which is able to handle the given command name
     */
    <C extends Command, R> CommandHandler<C, R> getHandlerFor(String commandName);

    /**
     * Returns {@code true} if the type is associated to an instance in this
     * container, false otherwise.
     * @param type type whose presence in this registry is to be tested
     * @return {@code true} if the type is associated to an instance in
     *         this container, false otherwise.
     */
    boolean containsHandler(Class<? extends CommandHandler> type);

    /**
     * Removes all the registered associations from the registry.
     */
    void clearRegistry();

    /**
     * Returns {@code true} if this registry has no stored associations.
     * @return {@code true} if this registry has no stored associations.
     */
    boolean isRegistryEmpty();

    /**
     * Returns the amount of type-instance associations currently in this registry.
     * @return the amount of type-instance associations currently in this registry.
     */
    int registrySize();

}
