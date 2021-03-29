package dev.sergheev.commandbus.registry;

import dev.sergheev.commandbus.Command;
import dev.sergheev.commandbus.CommandHandler;

/**
 * An object which represents a registry of {@link CommandHandler} instances.
 */
@SuppressWarnings({ "rawtypes" })
public interface CommandHandlerRegistry {

    /**
     * Returns the instance of {@link CommandHandler} associated to the command name.
     * @param commandName the class name of the command to be found
     * @return an instance of {@link CommandHandler}
     */
    <C extends Command, R> CommandHandler<C, R> findHandlerFor(String commandName);

    /**
     * Registers the specified {@link CommandHandler} instance with its specified type.
     *
     * @param type type with which the specified instance is to be associated
     * @param instance instance to be associated with the specified type
     */
    void registerHandler(Class<? extends CommandHandler> type, Object instance);

    /**
     * Unregisters the {@link CommandHandler} instance associated to given type.
     * @param type type with which the specified instance is to be associated
     */
    void unregisterHandler(Class<? extends CommandHandler> type);

    /**
     * Returns the instance to which the specified type is associated.
     * @param type type with which the specified instance is to be associated
     * @return the {@link CommandHandler} instance to which the specified type is mapped
     */
    <T extends CommandHandler> T getHandler(Class<T> type);

    /**
     * @return {@code true} if this registry has no {@link CommandHandler} registered.
     */
    boolean isRegistryEmpty();

    /**
     * @return the amount of {@link CommandHandler} instances registered.
     */
    int registrySize();

}
