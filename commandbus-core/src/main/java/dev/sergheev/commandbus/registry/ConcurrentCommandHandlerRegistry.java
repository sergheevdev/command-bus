package dev.sergheev.commandbus.registry;

import dev.sergheev.commandbus.Command;
import dev.sergheev.commandbus.CommandHandler;
import dev.sergheev.commandbus.container.Container;
import dev.sergheev.commandbus.mapping.CommandNameExtractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Objects.requireNonNull;

/**
 * A thread-safe {@link CommandHandlerRegistry} implementation.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ConcurrentCommandHandlerRegistry implements CommandHandlerRegistry {

    /**
     * A container that stores handler type to instance associations.
     */
    private final Container handlerContainer;

    /**
     * A map that associates a command name to the handler that can process that given command.
     */
    private final Map<String, Class<? extends CommandHandler>> commandNameToType;

    /**
     * An utility that extracts fully qualified command class names from mapping annotations in a given class.
     */
    private final CommandNameExtractor commandNameExtractor;

    /**
     * Ensures the atomicity of two actions: command name to handler mapping and the storage or retrieval of handlers.
     */
    private final Lock registryLock;

    /**
     * @throws AssertionError if an attempt to instantiate {@code ConcurrentCommandHandlerRegistry} is made
     */
    private ConcurrentCommandHandlerRegistry() {
        throw new AssertionError();
    }

    /**
     * Constructs a new {@link ConcurrentCommandHandlerRegistry} instance.
     * @param handlerContainer the container that will hold {@link CommandHandler} instances
     * @throws NullPointerException if the {@code handlerContainer} is {@code null}
     */
    ConcurrentCommandHandlerRegistry(Container handlerContainer) throws NullPointerException {
        requireNonNull(handlerContainer, "handlerContainer must not be null");
        this.handlerContainer = handlerContainer;
        this.commandNameToType = new HashMap<>();
        this.commandNameExtractor = new CommandNameExtractor();
        this.registryLock = new ReentrantLock();
    }

    /**
     * Stores the specified type-instance relationship in this registry.
     * @param type type with which the instance is to be associated
     * @param instance instance to be associated with the specified type
     * @param <T> the type of the instance
     * @throws NullPointerException if {@code type} is {@code null}
     * @throws NullPointerException if {@code instance} is {@code null}
     * @return the newly registered instance associated to {@code type}
     */
    @Override
    public <T extends CommandHandler> T registerHandler(Class<T> type, Object instance) throws NullPointerException {
        requireNonNull(type, "type must not be null");
        requireNonNull(instance, "instance must not be null");
        List<String> commandNames = commandNameExtractor.extractCommandNamesFor(type);
        registryLock.lock();
        try {
            commandNames.forEach(name -> commandNameToType.put(name, type));
            return handlerContainer.put(type, instance);
        } finally {
            registryLock.unlock();
        }
    }

    /**
     * Removes the instance to which the type is associated from this registry.
     * @param type type whose instance of to be removed from this registry
     * @param <T> the type of the value
     * @throws NullPointerException if {@code type} is {@code null}
     * @return the previous instance associated with {@code type}, or {@code null}
     *         if there was no instance associated to {@code key}.
     */
    @Override
    public <T extends CommandHandler> T unregisterHandler(Class<T> type) throws NullPointerException {
        requireNonNull(type, "type must not be null");
        List<String> commandNames = commandNameExtractor.extractCommandNamesFor(type);
        registryLock.lock();
        try {
            commandNames.forEach(commandNameToType::remove);
            return handlerContainer.remove(type);
        } finally {
            registryLock.unlock();
        }
    }

    /**
     * Returns the instance to which the specified key is associated.
     * @param type type whose associated instance is to be returned
     * @param <T> the type of the value
     * @throws NullPointerException if {@code type} is {@code null}
     * @return the current instance associated to {@code type}, or
     *         {@code null} if there was no association for {@code type}
     */
    @Override
    public <T extends CommandHandler> T getHandler(Class<T> type) throws NullPointerException {
        requireNonNull(type, "type must not be null");
        registryLock.lock();
        try {
            return handlerContainer.get(type);
        } finally {
            registryLock.unlock();
        }
    }

    /**
     * Returns the instance which is able to handle a given command name.
     * @param commandName command class name
     * @param <C> type of the command
     * @param <R> type of the handler's response
     * @throws NullPointerException if {@code commandName} is {@code null}
     * @return the instance which is able to handle the given command name
     */
    @Override
    public <C extends Command, R> CommandHandler<C, R> getHandlerFor(String commandName) throws NullPointerException {
        requireNonNull(commandName, "commandName must not be null");
        registryLock.lock();
        try {
            Class<? extends CommandHandler> handlerType = commandNameToType.get(commandName);
            return handlerContainer.get(handlerType);
        } finally {
            registryLock.unlock();
        }
    }

    /**
     * Returns {@code true} if the type is associated to an instance in this
     * container, false otherwise.
     * @param type type whose presence in this registry is to be tested
     * @throws NullPointerException if {@code type} is {@code null}
     * @return {@code true} if the type is associated to an instance in this
     *         container, false otherwise.
     */
    @Override
    public boolean containsHandler(Class<? extends CommandHandler> type) throws NullPointerException {
        requireNonNull(type, "type must not be null");
        registryLock.lock();
        try {
            return handlerContainer.contains(type);
        } finally {
            registryLock.unlock();
        }
    }

    /**
     * Removes all the registered associations from the registry.
     */
    @Override
    public void clearRegistry() {
        registryLock.lock();
        try {
            handlerContainer.clear();
        } finally {
            registryLock.unlock();
        }
    }

    /**
     * Returns {@code true} if this registry has no stored associations.
     * @return {@code true} if this registry has no stored associations.
     */
    @Override
    public boolean isRegistryEmpty() {
        registryLock.lock();
        try {
            return handlerContainer.isEmpty();
        } finally {
            registryLock.unlock();
        }
    }

    /**
     * Returns the amount of type-instance associations currently in this registry.
     * @return the amount of type-instance associations currently in this registry.
     */
    @Override
    public int registrySize() {
        registryLock.lock();
        try {
            return handlerContainer.size();
        } finally {
            registryLock.unlock();
        }
    }

}
