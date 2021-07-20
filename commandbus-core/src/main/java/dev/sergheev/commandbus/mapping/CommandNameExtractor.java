package dev.sergheev.commandbus.mapping;

import dev.sergheev.commandbus.Command;

import java.util.LinkedList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * A simple decorator for {@link CommandMappingExtractor} that extracts all the
 * command names (fully qualified commands class names) from {@link CommandMapping}
 * annotations present in that class.
 */
public class CommandNameExtractor {

    /**
     * Extracts command mappings from a given class.
     */
    private final CommandMappingExtractor mappingExtractor;

    public CommandNameExtractor() {
        this.mappingExtractor = new CommandMappingExtractor();
    }

    /**
     * Extracts a list of command names present in the mappings of a given class.
     * @param givenClass the given class in which command names are to be searched
     * @throws NullPointerException if {@code givenClass} is {@code null}
     * @return a list of command names associated to {@code givenClass}
     */
    public List<String> extractCommandNamesFor(Class<?> givenClass) {
        requireNonNull(givenClass, "givenClass must not be null");
        final List<String> commandNames = new LinkedList<>();
        final List<CommandMapping> mappings = mappingExtractor.extractMappingsFrom(givenClass);
        mappings.forEach(mapping -> {
            if(mapping.value() != Command.class) commandNames.add(mapping.value().getName());
        });
        return commandNames;
    }

}
