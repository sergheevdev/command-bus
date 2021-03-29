package dev.sergheev.commandbus.registry.mapping;

import dev.sergheev.commandbus.CommandMapping;
import dev.sergheev.commandbus.CommandMappings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * An object that extracts {@link CommandMapping} annotations from a class.
 */
public class CommandHandlerMappingExtractor {

    /**
     * Extracts all the {@link CommandMapping} present in the given class.
     *
     * @param givenClass the class from which mappings will be extracted
     *
     * @throws NullPointerException if {@code givenClass} is {@code null}
     *
     * @return a list of {@link CommandMapping}
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
