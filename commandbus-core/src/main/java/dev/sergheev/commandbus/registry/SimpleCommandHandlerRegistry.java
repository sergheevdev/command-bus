package dev.sergheev.commandbus.registry;

import dev.sergheev.commandbus.Command;
import dev.sergheev.commandbus.CommandHandler;
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

    private final Container handlerContainer;
    private final Map<String, Class<? extends CommandHandler>> commandNameToType;
    private final CommandNameExtractor commandNameExtractor;

    private SimpleCommandHandlerRegistry() {
        throw new AssertionError();
    }

    SimpleCommandHandlerRegistry(Container handlerContainer) {
        this.handlerContainer = requireNonNull(handlerContainer, "handlerContainer");
        this.commandNameToType = new HashMap<>();
        this.commandNameExtractor = new CommandNameExtractor();
    }

    @Override
    public <T extends CommandHandler> T registerHandler(Class<T> type, Object instance) {
        requireNonNull(type, "type");
        requireNonNull(instance, "instance");
        List<String> commandNames = commandNameExtractor.extractCommandNamesFor(type);
        commandNames.forEach(name -> commandNameToType.put(name, type));
        return handlerContainer.put(type, instance);
    }

    @Override
    public <T extends CommandHandler> T unregisterHandler(Class<T> type) {
        requireNonNull(type, "type");
        List<String> commandNames = commandNameExtractor.extractCommandNamesFor(type);
        commandNames.forEach(commandNameToType::remove);
        return handlerContainer.remove(type);
    }

    @Override
    public <T extends CommandHandler> T getHandler(Class<T> type) {
        requireNonNull(type, "type");
        return handlerContainer.get(type);
    }

    @Override
    public <C extends Command, R> CommandHandler<C, R> getHandlerFor(String commandName) {
        requireNonNull(commandName, "commandName");
        Class<? extends CommandHandler> handlerType = commandNameToType.get(commandName);
        return handlerContainer.get(handlerType);
    }

    @Override
    public boolean containsHandler(Class<? extends CommandHandler> type) {
        requireNonNull(type, "type");
        return handlerContainer.contains(type);
    }

    @Override
    public void clearRegistry() {
        handlerContainer.clear();
    }

    @Override
    public boolean isRegistryEmpty() {
        return handlerContainer.isEmpty();
    }

    @Override
    public int registrySize() {
        return handlerContainer.size();
    }

}
