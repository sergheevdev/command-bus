package dev.sergheev.commandbus.mapping;

import dev.sergheev.commandbus.CommandMapping;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * A simple decorator for {@link CommandMappingExtractor} that extracts command names
 * from {@link CommandMapping} present in a class.
 *
 * <p>The purpose of this decorator is to add another layer of abstraction to extract
 * command names from mappings, but first we need to obtain those mappings, which is
 * done by the decorated {@link CommandMappingExtractor} class.
 *
 * @see <a href="https://www.oreilly.com/library/view/design-patterns-elements/0201633612/">Design Patterns: Elements of Reusable Object-Oriented Software Structural Patterns Decorator
 */
public class CommandNameExtractor {

    private final CommandMappingExtractor mappingExtractor;

    public CommandNameExtractor() {
        this.mappingExtractor = new CommandMappingExtractor();
    }

    /**
     * Extracts a list of command names present in the mappings of the given class.
     *
     * @param givenClass the given class in which command names searched
     *
     * @throws NullPointerException if {@code givenClass} is {@code null}
     *
     * @return a list of command names associated to {@code givenClass}
     */
    public List<String> extractCommandNamesFor(Class<?> givenClass) {
        requireNonNull(givenClass, "givenClass");
        List<String> commandNames = new ArrayList<>();
        List<CommandMapping> mappings = mappingExtractor.extractMappingsFrom(givenClass);
        mappings.forEach(mapping -> commandNames.add(mapping.value().getName()));
        return commandNames;
    }

}