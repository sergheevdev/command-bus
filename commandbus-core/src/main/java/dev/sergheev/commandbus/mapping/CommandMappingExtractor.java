package dev.sergheev.commandbus.mapping;

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
     * @return a list of {@link CommandMapping} present in a given class
     */
    public List<CommandMapping> extractMappingsFrom(Class<?> givenClass) {
        requireNonNull(givenClass, "givenClass must not be null");
        final List<CommandMapping> foundMappings = new LinkedList<>();
        if(givenClass.isAnnotationPresent(CommandMapping.class)) {
            final CommandMapping mapping = givenClass.getAnnotation(CommandMapping.class);
            foundMappings.add(mapping);
        }
        else if(givenClass.isAnnotationPresent(CommandMappings.class)) {
            final CommandMappings multiMappings = givenClass.getAnnotation(CommandMappings.class);
            final List<CommandMapping> mappingGroup = Arrays.asList(multiMappings.value());
            foundMappings.addAll(mappingGroup);
        }
        return foundMappings;
    }

}
