package dev.sergheev.commandbus.mapping;

import dev.sergheev.commandbus.Command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A group of mappings that store the fully qualified {@link Command} class names.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandMappings {

    CommandMapping[] value();

}