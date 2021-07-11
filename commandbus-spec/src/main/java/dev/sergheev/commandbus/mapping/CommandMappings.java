package dev.sergheev.commandbus.mapping;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandMappings {

    CommandMapping[] value();

}