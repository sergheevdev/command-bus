package dev.sergheev.commandbus.registry;

import dev.sergheev.commandbus.Command;
import dev.sergheev.commandbus.CommandHandler;
import dev.sergheev.commandbus.container.HeterogeneousContainer;
import dev.sergheev.commandbus.registry.mapping.CommandHandlerMappingExtractorDecorator;
import dev.sergheev.commandbus.container.CommandHandlerContainer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Non-thread safe {@link CommandHandlerRegistry} implementation.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SimpleCommandHandlerRegistry implements CommandHandlerRegistry {

    public static SimpleCommandHandlerRegistry newInstance() {
        return new SimpleCommandHandlerRegistry(CommandHandlerContainer.newInstance(), HashMap::new);
    }

    private final HeterogeneousContainer commandHandlerContainer;

    private final Map<String, Class<? extends CommandHandler>> commandNameToHandlerClass;

    private final CommandHandlerMappingExtractorDecorator commandNameFromMappingExtractor;

    /**
     * Suppress default constructor for non-instantiability.
     */
    private SimpleCommandHandlerRegistry() {
        throw new AssertionError();
    }

    protected SimpleCommandHandlerRegistry(HeterogeneousContainer commandHandlerContainer,
                                           Supplier<Map<String, Class<? extends CommandHandler>>> mapSupplier) {
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
        Class<? extends CommandHandler> type = commandNameToHandlerClass.get(commandName);
        return commandHandlerContainer.get(type);
    }

    @Override
    public void registerHandler(Class<? extends CommandHandler> type, Object instance) {
        requireNonNull(type, "type");
        List<String> commandNames = commandNameFromMappingExtractor.extractCommandNamesFor(type);
        commandNames.forEach(name -> commandNameToHandlerClass.put(name, type));
        commandHandlerContainer.put(type, instance);
    }

    @Override
    public void unregisterHandler(Class<? extends CommandHandler> type) {
        requireNonNull(type, "type");
        List<String> commandNames = commandNameFromMappingExtractor.extractCommandNamesFor(type);
        commandNames.forEach(commandNameToHandlerClass::remove);
        commandHandlerContainer.remove(type);
    }

    @Override
    public <T extends CommandHandler> T getHandler(Class<T> type) {
        return commandHandlerContainer.get(type);
    }

    @Override
    public boolean isRegistryEmpty() {
        return commandHandlerContainer.isEmpty();
    }

    @Override
    public int registrySize() {
        return commandHandlerContainer.size();
    }

}
