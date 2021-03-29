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
 * Thread-safe {@link CommandHandlerRegistry} implementation.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ConcurrentCommandHandlerRegistry implements CommandHandlerRegistry {

    private final Container handlerContainer;

    private final Map<String, Class<? extends CommandHandler>> commandNameToType;

    private final CommandNameExtractor commandNameExtractor;

    private final Lock registryLock;

    private ConcurrentCommandHandlerRegistry() {
        throw new AssertionError();
    }

    ConcurrentCommandHandlerRegistry(Container handlerContainer) {
        this.handlerContainer = requireNonNull(handlerContainer);
        this.commandNameToType = new HashMap<>();
        this.commandNameExtractor = new CommandNameExtractor();
        this.registryLock = new ReentrantLock();
    }

    @Override
    public <T extends CommandHandler> T registerHandler(Class<T> type, Object instance) {
        requireNonNull(type, "type");
        List<String> commandNames = commandNameExtractor.extractCommandNamesFor(type);
        registryLock.lock();
        try {
            commandNames.forEach(name -> commandNameToType.put(name, type));
            return handlerContainer.put(type, instance);
        } finally {
            registryLock.unlock();
        }
    }

    @Override
    public <T extends CommandHandler> T unregisterHandler(Class<T> type) {
        requireNonNull(type, "type");
        List<String> commandNames = commandNameExtractor.extractCommandNamesFor(type);
        registryLock.lock();
        try {
            commandNames.forEach(commandNameToType::remove);
            return handlerContainer.remove(type);
        } finally {
            registryLock.unlock();
        }
    }

    @Override
    public <T extends CommandHandler> T getHandler(Class<T> type) {
        requireNonNull(type, "type");
        registryLock.lock();
        try {
            return handlerContainer.get(type);
        } finally {
            registryLock.unlock();
        }
    }

    @Override
    public <C extends Command, R> CommandHandler<C, R> getHandlerFor(String commandName) {
        requireNonNull(commandName, "commandName");
        registryLock.lock();
        try {
            Class<? extends CommandHandler> handlerType = commandNameToType.get(commandName);
            return handlerContainer.get(handlerType);
        } finally {
            registryLock.unlock();
        }
    }

    @Override
    public boolean containsHandler(Class<? extends CommandHandler> type) {
        requireNonNull(type, "type");
        registryLock.lock();
        try {
            return handlerContainer.contains(type);
        } finally {
            registryLock.unlock();
        }
    }

    @Override
    public void clearRegistry() {
        registryLock.lock();
        try {
            handlerContainer.clear();
        } finally {
            registryLock.unlock();
        }
    }

    @Override
    public boolean isRegistryEmpty() {
        registryLock.lock();
        try {
            return handlerContainer.isEmpty();
        } finally {
            registryLock.unlock();
        }
    }

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
