package dev.sergheev.commandbus;

/**
 * A simple DTO (Data Transfer Object) that contains the message to be printed.
 */
public class PrintLineCommand implements Command {

    private final String message;

    public PrintLineCommand(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}