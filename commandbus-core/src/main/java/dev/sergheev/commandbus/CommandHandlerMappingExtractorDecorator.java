package dev.sergheev.commandbus;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * A simple decorator around the {@link CommandHandlerMappingExtractor} class.
 *
 * <p>The purpose of this decorator implementation is to add another layer of abstraction to
 * {@link CommandHandlerMappingExtractor}, because we want to extract command names from
 * mappings, but first we need to obtain those mappings, which is done by the decorated class
 * {@link CommandHandlerMappingExtractor}.</p>
 *
 * @see <a href="https://www.oreilly.com/library/view/design-patterns-elements/0201633612/">Design Patterns: Elements of Reusable Object-Oriented Software Structural Patterns Decorator
 */
public class CommandHandlerMappingExtractorDecorator {

    private final CommandHandlerMappingExtractor mappingExtractor;

    public CommandHandlerMappingExtractorDecorator() {
        this.mappingExtractor = new CommandHandlerMappingExtractor();
    }

    public CommandHandlerMappingExtractorDecorator(CommandHandlerMappingExtractor mappingExtractor) {
        this.mappingExtractor = new CommandHandlerMappingExtractor();
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
        List<CommandMapping> mappings = mappingExtractor.extractCommandMappingsFor(givenClass);
        mappings.forEach(mapping -> commandNames.add(mapping.value().getName()));
        return commandNames;
    }

}
