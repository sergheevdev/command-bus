package dev.sergheev.commandbus.registry;

import dev.sergheev.commandbus.Command;
import dev.sergheev.commandbus.CommandHandler;
import dev.sergheev.commandbus.container.HeterogeneousContainer;
import dev.sergheev.commandbus.registry.mapping.CommandHandlerMappingExtractorDecorator;
import dev.sergheev.commandbus.container.CommandHandlerContainer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Thread safe {@link CommandHandlerRegistry} implementation.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ConcurrentCommandHandlerRegistry implements CommandHandlerRegistry {

    public static ConcurrentCommandHandlerRegistry newInstance() {
        return new ConcurrentCommandHandlerRegistry(CommandHandlerContainer.newInstance(), HashMap::new);
    }

    private final Lock registryLock;

    private final HeterogeneousContainer commandHandlerContainer;

    private final Map<String, Class<? extends CommandHandler>> commandNameToHandlerClass;

    private final CommandHandlerMappingExtractorDecorator commandNameFromMappingExtractor;

    /**
     * Suppress default constructor for non-instantiability.
     */
    private ConcurrentCommandHandlerRegistry() {
        throw new AssertionError();
    }

    protected ConcurrentCommandHandlerRegistry(HeterogeneousContainer commandHandlerContainer,
                                               Supplier<Map<String, Class<? extends CommandHandler>>> mapSupplier) {
        this.registryLock = new ReentrantLock();
        this.commandHandlerContainer = requireNonNull(commandHandlerContainer);
        this.commandNameToHandlerClass = asEmptyMap(mapSupplier);
        this.commandNameFromMappingExtractor = new CommandHandlerMappingExtractorDecorator();
    }

    private Map<String, Class<? extends CommandHandler>> asEmptyMap(Supplier<Map<String, Class<? extends CommandHandler>>> mapSupplier) {
        Map<String, Class<? extends CommandHandler>> values = mapSupplier.get();
        final String message = "The provided supplier must be empty: mapSupplier";
        if(!values.isEmpty()) throw new IllegalArgumentException(message);
        return values;
    }

    @Override
    public <C extends Command, R> CommandHandler<C, R> findHandlerFor(String commandName) {
        requireNonNull(commandName, "commandName");
        registryLock.lock();
        try {
            Class<? extends CommandHandler> type = commandNameToHandlerClass.get(commandName);
            return commandHandlerContainer.get(type);
        } finally {
            registryLock.unlock();
        }
    }

    @Override
    public void registerHandler(Class<? extends CommandHandler> type, Object instance) {
        requireNonNull(type, "type");
        List<String> commandNames = commandNameFromMappingExtractor.extractCommandNamesFor(type);
        registryLock.lock();
        try {
            commandNames.forEach(name -> commandNameToHandlerClass.put(name, type));
            commandHandlerContainer.put(type, instance);
        } finally {
            registryLock.unlock();
        }
    }

    @Override
    public void unregisterHandler(Class<? extends CommandHandler> type) {
        requireNonNull(type, "type");
        List<String> commandNames = commandNameFromMappingExtractor.extractCommandNamesFor(type);
        registryLock.lock();
        try {
            commandNames.forEach(commandNameToHandlerClass::remove);
            commandHandlerContainer.remove(type);
        } finally {
            registryLock.unlock();
        }
    }

    @Override
    public <T extends CommandHandler> T getHandler(Class<T> type) {
        registryLock.lock();
        try {
            return commandHandlerContainer.get(type);
        } finally {
            registryLock.unlock();
        }
    }

    @Override
    public boolean isRegistryEmpty() {
        registryLock.lock();
        try {
            return commandHandlerContainer.isEmpty();
        } finally {
            registryLock.unlock();
        }
    }

    @Override
    public int registrySize() {
        registryLock.lock();
        try {
            return commandHandlerContainer.size();
        } finally {
            registryLock.unlock();
        }
    }

}
