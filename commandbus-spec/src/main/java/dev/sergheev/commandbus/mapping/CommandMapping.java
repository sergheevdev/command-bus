package dev.sergheev.commandbus.mapping;

import dev.sergheev.commandbus.Command;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A mapping that stores the fully qualified {@link Command} class name.
 */
@Repeatable(CommandMappings.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandMapping {

    Class<? extends Command> value();

}