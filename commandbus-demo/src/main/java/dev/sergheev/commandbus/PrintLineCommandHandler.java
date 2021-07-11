package dev.sergheev.commandbus;

import dev.sergheev.commandbus.mapping.CommandMapping;

/**
 * A handler that performs the correspondent action for {@link PrintLineCommand}.
 */
@CommandMapping(PrintLineCommand.class)
public class PrintLineCommandHandler implements CommandHandler<PrintLineCommand, Boolean> {

    public Boolean handle(PrintLineCommand command) {
        System.out.println(command.getMessage());
        return true;
    }

}