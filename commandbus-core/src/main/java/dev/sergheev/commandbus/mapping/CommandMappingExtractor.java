package dev.sergheev.commandbus.mapping;

import dev.sergheev.commandbus.CommandMapping;
import dev.sergheev.commandbus.CommandMappings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * An object that extracts {@link CommandMapping} annotations from a class.
 */
public class CommandMappingExtractor {

    /**
     * Extracts all the {@link CommandMapping} present in a given class.
     * @param givenClass the class from which all {@link CommandMapping} will be extracted
     * @throws NullPointerException if {@code givenClass} is {@code null}
     * @return a list of {@link CommandMapping}
     */
    public List<CommandMapping> extractMappingsFrom(Class<?> givenClass) {
        requireNonNull(givenClass, "givenClass");
        List<CommandMapping> foundMappings = new LinkedList<>();
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
