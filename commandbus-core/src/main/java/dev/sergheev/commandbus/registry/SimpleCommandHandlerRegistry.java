package dev.sergheev.commandbus.registry;

import dev.sergheev.commandbus.Command;
import dev.sergheev.commandbus.CommandHandler;
import dev.sergheev.commandbus.SimpleCommandBus;
import dev.sergheev.commandbus.container.Container;
import dev.sergheev.commandbus.mapping.CommandNameExtractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * A non thread-safe {@link CommandHandlerRegistry} implementation.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SimpleCommandHandlerRegistry implements CommandHandlerRegistry {

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
     * @throws AssertionError if an attempt to instantiate {@code SimpleCommandHandlerRegistry} is made
     */
    private SimpleCommandHandlerRegistry() {
        throw new AssertionError();
    }

    /**
     * Constructs a new {@link SimpleCommandHandlerRegistry} instance.
     * @param handlerContainer the container that will hold {@link CommandHandler} instances
     * @throws NullPointerException if the {@code handlerContainer} is {@code null}
     */
    SimpleCommandHandlerRegistry(Container handlerContainer) throws NullPointerException {
        requireNonNull(handlerContainer, "handlerContainer must not be null");
        this.handlerContainer = handlerContainer;
        this.commandNameToType = new HashMap<>();
        this.commandNameExtractor = new CommandNameExtractor();
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
        commandNames.forEach(name -> commandNameToType.put(name, type));
        return handlerContainer.put(type, instance);
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
        commandNames.forEach(commandNameToType::remove);
        return handlerContainer.remove(type);
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
        return handlerContainer.get(type);
    }

    /**
     * Returns the instance which is able to handle a given command name.
     * @param commandName the name of the command (fully qualified {@link Command} class name)
     * @param <C> type of the command
     * @param <R> type of the handler's response
     * @throws NullPointerException if {@code commandName} is {@code null}
     * @return the instance which is able to handle the given command name
     */
    @Override
    public <C extends Command, R> CommandHandler<C, R> getHandlerFor(String commandName) throws NullPointerException {
        requireNonNull(commandName, "commandName must not be null");
        Class<? extends CommandHandler> handlerType = commandNameToType.get(commandName);
        return handlerContainer.get(handlerType);
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
        return handlerContainer.contains(type);
    }

    /**
     * Removes all the registered associations from the registry.
     */
    @Override
    public void clearRegistry() {
        handlerContainer.clear();
    }

    /**
     * Returns {@code true} if this registry has no stored associations.
     * @return {@code true} if this registry has no stored associations.
     */
    @Override
    public boolean isRegistryEmpty() {
        return handlerContainer.isEmpty();
    }

    /**
     * Returns the amount of type-instance associations currently in this registry.
     * @return the amount of type-instance associations currently in this registry.
     */
    @Override
    public int registrySize() {
        return handlerContainer.size();
    }

}
