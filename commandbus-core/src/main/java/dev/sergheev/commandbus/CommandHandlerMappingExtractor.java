package dev.sergheev.commandbus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * A {@link CommandMapping} extractor implementation.
 *
 * <p>A simple class that deals with the extraction of {@link CommandMapping}
 * annotations present in a given class</p>
 *
 */
public class CommandHandlerMappingExtractor {

    /**
     * Extracts a list of {@link CommandMapping} associated the given class.
     *
     * @param givenClass the given class from which mappings will be extracted
     *
     * @throws NullPointerException if {@code givenClass} is {@code null}
     *
     * @return a list of {@link CommandMapping} that {@code givenClass} has
     */
    public List<CommandMapping> extractCommandMappingsFor(Class<?> givenClass) {
        requireNonNull(givenClass, "givenClass");
        List<CommandMapping> foundMappings = new ArrayList<>();
        if(givenClass.isAnnotationPresent(CommandMapping.class)) {
            CommandMapping mapping = givenClass.getAnnotation(CommandMapping.class);
            foundMappings.add(mapping);
        }
        else if(givenClass.isAnnotationPresent(CommandMappings.class)) {
            CommandMappings multiMappings = givenClass.getAnnotation(CommandMappings.class);
            List<CommandMapping> mappingGroup = Arrays.asList(multiMappings.value());
            foundMappings.addAll(mappingGroup);
        }
        return foundMappings;
    }

}
